package com.lyf;


import com.lyf.Server.Server;
import com.lyf.Service.Impl.UserServiceImpl;
import com.lyf.Service.UserService;
import com.lyf.config.RegistryConfig;
import com.lyf.config.RpcConfig;
import com.lyf.model.ServiceMetaInfo;
import com.lyf.registry.LocalRegistry;
import com.lyf.registry.Registry;
import com.lyf.registry.RegistryFactory;
import com.lyf.tcp.VertxTcpServer;

/**
 * 服务提供者示例
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 TCP 服务
        Server server = new Server();
        server.doStart(RpcApplication.getConfig().getServerPort());
    }
}
