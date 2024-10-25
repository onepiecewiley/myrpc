package com.lyf;

import com.lyf.config.RegistryConfig;
import com.lyf.config.RpcConfig;
import com.lyf.constant.RpcConstant;
import com.lyf.registry.Registry;
import com.lyf.registry.RegistryFactory;
import com.lyf.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/18 17:59
 */
//仅仅只有加载RpcConfig还不够 因为不一定只加载这些基本参数 加载完毕后要做一些初始化工作等等 所以再写一个类 同时每个线程都应该只加载一次配置类 不能重复加载
//因此这里采取双检索单例模式来完成这个功能 初始化不仅仅是加载配置！！！ 初始化只应该完成一次
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static RpcConfig getConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    //加载配置 完成初始化
                    init();
                }
            }
        }
        return rpcConfig;
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());

        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config = {}",registryConfig);

        //创建并注册 Shutdown Hook, JVM退出的时候执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }
}
