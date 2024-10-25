package com.lyf.fault.tolerant;

import com.lyf.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:53
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常",e);
        return new RpcResponse();
    }
}
