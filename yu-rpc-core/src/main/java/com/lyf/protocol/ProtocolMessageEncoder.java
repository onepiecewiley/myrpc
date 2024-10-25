package com.lyf.protocol;

import com.lyf.serializer.Serializer;
import com.lyf.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

public class ProtocolMessageEncoder {

    /**
     * 编码
     *
     * @param protocolMessage
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
      if(protocolMessage == null || protocolMessage.getHeader() == null){
          return Buffer.buffer();
      }
      ProtocolMessage.Header header = protocolMessage.getHeader();

      //依次向缓冲区写入字节 vertx的tcp服务器发送的是Buffer对象
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        //获取序列化器 序列化要发送的内容 body里面
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serializer(protocolMessage.getBody());

        //写入body的数据到buffer 以及 body的长度
        buffer.appendInt(bodyBytes.length);
        buffer.appendBytes(bodyBytes);
        return buffer;
    }
}
