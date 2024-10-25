package com.lyf.serializer;


import java.io.IOException;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:48
 */
public interface Serializer {
    public <T> byte[] serializer(T request) throws IOException;

    public <T> Object deserializer(byte[] request, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException;
}
