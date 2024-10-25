package com.lyf.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/22 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     * */
    private Header header;

    /**
     * 消息体
     * */
    private T body;


    /**
     * 协议消息头
     * */
    @Data
    public static class Header{
        /**
         * 魔数
         * */
        private byte magic;

        /**
         * 版本号
         * */
        private byte version;

        /**
         * 序列化器
         * */
        private byte serializer;

        /**
         * 消息类型(请求/响应)
         * */
        private byte type;

        /**
         * 状态
         * */
        private byte status;

        /**
         *请求id
         *  */
        private long requestId;

        /**
         * 消息体长度
         * */
        private int bodyLength;
    }
}
