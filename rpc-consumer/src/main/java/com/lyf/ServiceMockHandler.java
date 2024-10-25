package com.lyf;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/19 14:07
 */
@Slf4j
public class ServiceMockHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();

        log.info("now,test mock method: {}",method.getName());
        return getDefaultReturn(returnType);
    }

    public Object getDefaultReturn(Class<?> returnType){
        if(returnType.isPrimitive()){
            if(returnType == int.class){
                return 0;
            }else if(returnType == short.class){
                return 0;
            }else if(returnType == boolean.class){
                return false;
            }else if(returnType == String.class){
                return "empty";
            }
        }
        return null;
    }
}
