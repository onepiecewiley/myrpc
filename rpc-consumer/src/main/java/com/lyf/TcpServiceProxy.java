package com.lyf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.lyf.config.RpcConfig;
import com.lyf.constant.RpcConstant;
import com.lyf.loadbalancer.LoadBalancer;
import com.lyf.loadbalancer.LoadBalancerFactory;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;
import com.lyf.model.ServiceMetaInfo;
import com.lyf.protocol.*;
import com.lyf.registry.Registry;
import com.lyf.registry.RegistryFactory;
import com.lyf.serializer.Serializer;
import com.lyf.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/22 15:26
 */
public class TcpServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());

        //构造请求
        String serviceName = method.getDeclaringClass().getName(); //获取该方法所在的类的名称 返回一个字符串，表示包含该方法的类的完全限定名（即包名 + 类名）
        RpcRequest rpcRequest = RpcRequest.builder().service(serviceName).method(method.getName())
                .parameterTypes(method.getParameterTypes()).args(args).build();
        try {
            //序列化
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            //从注册中心获取请求服务提供者的请求地址
            RpcConfig rpcConfig = RpcApplication.getConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            // 引入负载均衡策略
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名作为负载均衡参数
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethod());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);
            System.out.println("ServiceMetaInfo:  " + selectedServiceMetaInfo);

            //发送Tcp请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(),selectedServiceMetaInfo.getServiceHost(),
                    result -> {
                        if (result.succeeded()) {
                            System.out.println("Connected to TCP server");
                            io.vertx.core.net.NetSocket socket = result.result();
                            // 发送数据
                            // 构造消息
                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                            ProtocolMessage.Header header = new ProtocolMessage.Header();
                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getConfig().getSerializer()).getKey());
                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                            header.setRequestId(IdUtil.getSnowflakeNextId());
                            protocolMessage.setHeader(header);
                            protocolMessage.setBody(rpcRequest);
                            // 编码请求
                            try {
                                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                                socket.write(encodeBuffer);
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息编码错误");
                            }

                            // 接收响应
                            socket.handler(buffer -> {
                                try {
                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                                } catch (IOException | ClassNotFoundException | InstantiationException |
                                         IllegalAccessException e) {
                                    throw new RuntimeException("协议消息解码错误");
                                }
                            });
                        } else {
                            System.err.println("Failed to connect to TCP server");
                        }
                    });
            RpcResponse rpcResponse = responseFuture.get();
            // 记得关闭连接
            netClient.close();
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}



