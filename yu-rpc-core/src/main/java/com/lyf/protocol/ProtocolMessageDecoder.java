package com.lyf.protocol;

import com.google.protobuf.ProtocolMessageEnum;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;
import com.lyf.serializer.Serializer;
import com.lyf.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/22 14:42
 */
public class ProtocolMessageDecoder {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 分别从指定位置读出Buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        System.out.println("magic: " + magic);
        //校验魔数
        if(magic != ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息 magic非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength((buffer.getInt(13)));

        //解决粘包问题，只读取指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17,17 + header.getBodyLength());

        //解析消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("序列化消息的协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        // 确定数据的类型 是请求数据还是响应数据
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if(messageTypeEnum == null){
            throw new RuntimeException("序列化消息的类型不存在");
        }

        switch(messageTypeEnum) {
            case REQUEST:
                RpcRequest request = (RpcRequest) serializer.deserializer(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,request);
            case RESPONSE:
                RpcResponse response = (RpcResponse) serializer.deserializer(bodyBytes,RpcResponse.class);
                return new ProtocolMessage<>(header,response);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("该消息类型不支持");
        }
    }
}
