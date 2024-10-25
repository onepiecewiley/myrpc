package com.lyf.fault.retry;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:22
 */
public interface RetryStrategyKeys {
    /**
     * 不重试策略
     * */
    String NO = "no";

    /**
     * 固定时间间隔
     * */
    String FIXED_INTERVAL = "fixedInterval";
}
