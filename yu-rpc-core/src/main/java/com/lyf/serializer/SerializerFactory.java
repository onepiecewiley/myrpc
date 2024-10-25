package com.lyf.serializer;

import com.lyf.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/21 14:59
 */
public class SerializerFactory {

/*    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>();

    static {
        KEY_SERIALIZER_MAP.put(SerializerKeys.JDK,new JdkSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.JSON,new JsonSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.KRYO,new KryoSerializer());
        KEY_SERIALIZER_MAP.put(SerializerKeys.HESSIAN,new HessianSerializer());
    }

    //默认序列化器
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");

    //获取实例
    public static Serializer getInstance(String key){
        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }*/

    private static volatile Map<String,Class<?>> loadMap;  //双检索单例模式懒汉式加载
    /**
     * 默认序列化器*/
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例*/
    public static Serializer getInstance(String key){
        if(loadMap == null){
            synchronized (SerializerFactory.class){
                if(loadMap == null){
                    loadMap = SpiLoader.load(Serializer.class);
                }
            }
        }
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
