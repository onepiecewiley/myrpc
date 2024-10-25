package com.lyf.registry;

import com.lyf.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/22 10:34
 * 服务节点的信息更新并不是特别的频繁 所以服务消费者从注册中心获取服务节点信息列表后可以缓存在本地，下次请求不需要再向注册中心获取，从而来提高性能
 */
public class RegistryServiceCache {
    /**
     * 服务缓存
     * */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     * */
    void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache  = newServiceCache;
    }

    /**
     * 读缓存
     * */

    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    /**
     * 清空缓存
     * */
    void clearCache(){
        this.serviceCache = null;
    }
}
