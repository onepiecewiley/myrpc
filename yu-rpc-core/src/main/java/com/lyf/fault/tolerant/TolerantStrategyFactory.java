package com.lyf.fault.tolerant;

import com.lyf.spi.SpiLoader;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/24 14:58
 */
public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     * */
    private static final TolerantStrategy DEFAULT_RETRY_STRAGETY = new FailFastTolerantStrategy();

    /**
    * 获取实例
    * */
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class,key);
    }
}
