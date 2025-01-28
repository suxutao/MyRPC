package com.mine.myrpc.registry;

import com.mine.myrpc.model.ServiceMetaInfo;

import java.util.List;

public class RegistryServiceCache {
    //服务缓存
    static List<ServiceMetaInfo> serviceCache;

    //写缓存
    void writeCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    //读缓存
    List<ServiceMetaInfo> readCache() {
        return this.serviceCache;
    }

    //清空缓存
    void clearCache() {
        this.serviceCache = null;
    }
}
