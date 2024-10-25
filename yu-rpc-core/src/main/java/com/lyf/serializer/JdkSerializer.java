package com.lyf.serializer;

import java.io.*;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:52
 */
public class JdkSerializer implements Serializer{
    @Override
    public <T> byte[] serializer(T request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(request);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserializer(byte[] request,Class<T> cls) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object o = objectInputStream.readObject();
        try{
            return (T)o;
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
