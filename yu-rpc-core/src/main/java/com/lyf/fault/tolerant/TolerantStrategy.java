package com.lyf.fault.tolerant;

import com.lyf.model.RpcResponse;

import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:51
 * 第一个参数context代表上下文
 */
public interface TolerantStrategy {
    RpcResponse doTolerant(Map<String,Object> context, Exception e);
}
