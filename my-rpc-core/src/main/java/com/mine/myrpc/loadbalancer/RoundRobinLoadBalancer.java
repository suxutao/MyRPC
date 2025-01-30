package com.mine.myrpc.loadbalancer;

import com.mine.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//负载均衡——轮询算法
public class RoundRobinLoadBalancer implements LoadBalancer{

    //轮询下标
    private final static AtomicInteger currentIndex=new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }
        int size=serviceMetaInfoList.size();
        if (size==1){
            return serviceMetaInfoList.get(0);
        }
        int index=currentIndex.getAndIncrement()%size;
        return serviceMetaInfoList.get(index);
    }
}
