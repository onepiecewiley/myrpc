package com.lyf.loadbalancer;

import com.lyf.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/23 17:02
 */
public class RoundRobinLoaderBalancer implements LoadBalancer{

    /**
    * 轮询负载均衡器
    * */
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 只有一个服务的话就不需要轮询
        int size = serviceMetaInfoList.size();
        if(size == 1) {
            return serviceMetaInfoList.get(0);
        }
        //取模算法轮询
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
