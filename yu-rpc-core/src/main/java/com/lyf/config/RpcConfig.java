package com.lyf.config;

import com.lyf.fault.retry.RetryStrategy;
import com.lyf.fault.retry.RetryStrategyKeys;
import com.lyf.fault.tolerant.TolerantStrategyKeys;
import com.lyf.loadbalancer.LoadBalancer;
import com.lyf.loadbalancer.LoadBalancerKeys;
import com.lyf.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/18 17:40
 */
@Data
public class RpcConfig {

    /**
     *  开启测试 mock?
     */
    private boolean mock = false;
    /**
     * 名称
     */
    private String name = "yu-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8081;

    /**
     * 序列化器
    * */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     * */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
    * 负载均衡器
    * */
    private String loadBalancer = LoadBalancerKeys.RANDOM;

    /**
     * 重试策略
     * */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     * */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}