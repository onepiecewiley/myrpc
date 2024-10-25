package com.lyf.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian 序列化器
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class HessianSerializer implements Serializer {

    @Override
    public <T> byte[] serializer(T request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(bos);
        ho.writeObject(request);
        return bos.toByteArray();
    }

    @Override
    public <T> Object deserializer(byte[] request, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(request);
        HessianInput hi = new HessianInput(bis);
        return (T) hi.readObject(cls);
    }
}
