package com.lyf.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:35
 */
@Data
public class RpcResponse implements Serializable {

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应数据类型（预留）
     */
    private Class<?> dataType;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 异常信息
     */
    private Exception exception;

}
