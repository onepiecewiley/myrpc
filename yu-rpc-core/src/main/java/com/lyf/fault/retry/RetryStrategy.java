package com.lyf.fault.retry;

import com.lyf.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 13:50
 */
public interface RetryStrategy {
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
