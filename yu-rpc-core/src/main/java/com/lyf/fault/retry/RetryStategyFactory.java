package com.lyf.fault.retry;

import com.lyf.spi.SpiLoader;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:23
 */
public class RetryStategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }

    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy(); //默认的重试器

    /**
     * 获取实例
     * */
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
