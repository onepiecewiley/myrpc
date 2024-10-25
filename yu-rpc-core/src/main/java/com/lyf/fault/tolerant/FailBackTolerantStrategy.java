package com.lyf.fault.tolerant;

import com.lyf.model.RpcResponse;

import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:54
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //自行扩展
        return null;
    }
}
