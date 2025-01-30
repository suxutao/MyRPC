package com.mine.myrpc.loadbalancer;

import com.mine.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//负载均衡——一致性哈希算法
public class ConsistentHashLoadBalancer implements LoadBalancer{
    //一致性Hash环
    private final static TreeMap<Integer,ServiceMetaInfo>virtualNodes=new TreeMap<>();

    //虚拟节点数
    private static final int VIRTUAL_NODE_NUM=100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }
        //构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash=getHash(serviceMetaInfo.getServiceAddress()+"#"+i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }
        //获取调用请求的hash值
        int hash=getHash(requestParams);
        Map.Entry<Integer,ServiceMetaInfo>entry=virtualNodes.ceilingEntry(hash);
        if (entry==null){
            entry=virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}
