package com.lyf;

import java.lang.reflect.Proxy;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 13:40
 */
public class MyProxyFactory {

    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new ServiceMockHandler());
    }

    public static <T> T getProxy(Class<T> serviceClass) {
        if(RpcApplication.getConfig().isMock()){
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceHandler());
    }
}
