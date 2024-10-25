package com.lyf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lyf.config.RpcConfig;
import com.lyf.constant.RpcConstant;
import com.lyf.fault.retry.RetryStategyFactory;
import com.lyf.fault.retry.RetryStrategy;
import com.lyf.fault.tolerant.TolerantStrategy;
import com.lyf.fault.tolerant.TolerantStrategyFactory;
import com.lyf.loadbalancer.LoadBalancer;
import com.lyf.loadbalancer.LoadBalancerFactory;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;
import com.lyf.model.ServiceMetaInfo;
import com.lyf.registry.Registry;
import com.lyf.registry.RegistryFactory;
import com.lyf.serializer.Serializer;
import com.lyf.serializer.SerializerFactory;
import com.lyf.spi.SpiLoader;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 19:27
 */
public class ServiceHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());
        RpcRequest rpcRequest = RpcRequest.builder().service(method.getDeclaringClass().getName()).
                method(method.getName()).parameterTypes(method.getParameterTypes()).args(args).build();
        try {
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            RpcConfig rpcConfig = RpcApplication.getConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(method.getDeclaringClass().getName()); //设置服务名称
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodNamne",rpcRequest.getMethod());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println("serviceMetaInfo: " + serviceMetaInfo);
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            //获取重试策略
            RpcResponse rpcResponse = null;
            RetryStrategy retryStrategy = RetryStategyFactory.getInstance(rpcConfig.getRetryStrategy());
            try{
                rpcResponse = retryStrategy.doRetry(new Callable<RpcResponse>() {
                    @Override
                    public RpcResponse call() throws Exception {
                        byte[] result;
                        try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                                .body(bodyBytes)
                                .execute()) {
                            result = httpResponse.bodyBytes();
                        }
                        return (RpcResponse) serializer.deserializer(result, RpcResponse.class);
                    }
                });
            }catch (Exception e) {
                //容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
                tolerantStrategy.doTolerant(null,e);
            }
//            System.out.println("执行成功!!!!!在有重试策略的情况下!!!!!");
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
