package com.lyf.fault.tolerant;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:56
 */
public interface TolerantStrategyKeys {
    /**
     * 故障恢复
     * */
    String FAIL_BACK = "failBack";

    /**
     * 快速失败
     * */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     * */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     * */
    String FAIL_SAFE = "failSafe";
}

