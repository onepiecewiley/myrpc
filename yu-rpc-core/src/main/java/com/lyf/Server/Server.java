package com.lyf.Server;

import com.lyf.RpcApplication;
import com.lyf.dao.User;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;
import com.lyf.registry.LocalRegistry;
import com.lyf.serializer.Serializer;
import com.lyf.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:37
 */
public class Server implements MyServer{
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        //创建http服务器 写明请求处理的方法 同时监听接口
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.requestHandler(request ->{
            //处理请求 先打印
            System.out.println("接收到了请求: " + request.method() + request.uri());

            //需要根据请求参数进行服务调用返回结果

            request.bodyHandler(body ->{
                RpcRequest rpcRequest = null;
                byte[] bytes = body.getBytes();
                Serializer serializer = SerializerFactory.getInstance(RpcApplication.getConfig().getSerializer());
                try {
                    rpcRequest = (RpcRequest) serializer.deserializer(bytes,RpcRequest.class);
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
                //拿到这个之后要反序列化
                String service = rpcRequest.getService();
                //通过名称获取Class对象
                //System.out.println(service);
                Class<?> cls = LocalRegistry.get(service);
                //System.out.println(cls);
                String method = rpcRequest.getMethod();
                Object[] args = rpcRequest.getArgs();
                Class<?>[] params = rpcRequest.getParameterTypes();
                //用反射的机制去生成一个对象 调用方法 得到结果返回
                RpcResponse rpcReponse = new RpcResponse();
                Class<?> implClass = null;
                Method mmethod = null;
                User result = null;
                try {
                    implClass = LocalRegistry.get(rpcRequest.getService());
                    mmethod = implClass.getMethod(rpcRequest.getMethod(), rpcRequest.getParameterTypes());
                    //System.out.println("参数类型: " + rpcRequest.getArgs());
                    result = (User) mmethod.invoke(implClass.newInstance(), rpcRequest.getArgs());
                } catch (Exception e) {
                    rpcReponse.setMessage(e.getMessage());
                    rpcReponse.setException(e);
                    throw new RuntimeException(e);
                }
                rpcReponse.setData(result);
                rpcReponse.setDataType(mmethod.getReturnType());
                //得到结果之后要返回
                //发送http响应请求
                try {
                    byte[] response = serializer.serializer(rpcReponse);
                    request.response().putHeader("content-type","text/plain").end(Buffer.buffer(response));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        httpServer.listen(port,request->{
            if(request.succeeded()){
                System.out.println("成功监听: "+port+"端口");
            }else{
                System.out.println("监听失败");
            }
        });
    }
}
