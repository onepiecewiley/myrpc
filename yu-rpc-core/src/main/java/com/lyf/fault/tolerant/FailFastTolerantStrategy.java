package com.lyf.fault.tolerant;

import com.lyf.model.RpcResponse;

import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:52
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务错误",e);
    }
}
