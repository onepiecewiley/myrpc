package com.lyf.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;

import java.io.IOException;

/**
 * Json 序列化器
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class JsonSerializer implements Serializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public <T> byte[] serializer(T request) throws IOException {
        //序列化
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(request);
        return bytes;
    }

    @Override
    public <T> Object deserializer(byte[] request, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        T obj = OBJECT_MAPPER.readValue(request,cls);
        if(obj instanceof RpcRequest){
            return handleRequest((RpcRequest)obj,cls);
        }

        if(obj instanceof RpcResponse){
            return handleResponse((RpcResponse)obj,cls);
        }
        return obj;
    }

    private <T> Object handleResponse(RpcResponse rpcResponse, Class<T> cls) throws IOException {
        //处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes,rpcResponse.getDataType()));
        return cls.cast(rpcResponse);
    }


    // Object类型的原始对象在序列化的时候会被擦除 导致反序列化的时候会被作为LinkedHashMap无法转换成原始对象 所以需要特殊处理
    private <T> T handleRequest(RpcRequest rpcRequest,Class<T> type) throws IOException {
        Class<?>[] parameterType = rpcRequest.getParameterTypes();  //获取参数类型列表 即调用方法的参数类型列表
        Object[] args = rpcRequest.getArgs(); //获取参数列表

        for(int i = 0;i < parameterType.length;i++){
            Class clz = parameterType[i];
            if(!clz.isAssignableFrom(args[i].getClass())) {
                //判断一个类或接口是否可以赋值给另一个类或接口。它通常用于检查类的兼容性或类型检查。
                // 如果 A 是 B 的父类或父接口，或者 A 与 B 是同一个类，isAssignableFrom 返回 true。
                //A.isAssignableFrom(B) 表示类 B 是否可以赋值给类 A。
                byte[] argsType = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argsType, clz);
            }
        }
        return type.cast(rpcRequest); //将对象强制转换为指定的类型 这里就是强制转换成RpcRequest 但其实现在传入的已经是RpcRequest类型了
    }
}
