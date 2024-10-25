package com.lyf.fault.retry;

import com.lyf.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 13:51
 */
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call(); //一次调用 不成功则失败
    }
}
