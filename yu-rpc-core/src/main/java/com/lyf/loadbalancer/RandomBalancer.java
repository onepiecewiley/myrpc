package com.lyf.loadbalancer;

import com.lyf.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/23 17:06
 */
public class RandomBalancer implements LoadBalancer{
    private final Random random = new Random();
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if(size == 0){
            return null;
        }
        //只有1个服务，不用随机
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
