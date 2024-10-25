package com.lyf;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/18 18:16
 */

import com.lyf.config.RpcConfig;
import com.lyf.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "META-INF/rpc");
        System.out.println(rpc);
    }
}
