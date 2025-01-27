package com.mine.myrpc;

import com.mine.myrpc.config.RegistryConfig;
import com.mine.myrpc.config.RpcConfig;
import com.mine.myrpc.constant.RpcConstant;
import com.mine.myrpc.registry.EtcdRegistry;
import com.mine.myrpc.registry.Registry;
import com.mine.myrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    //框架初始化
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig=rpcConfig.getRegistryConfig();
        Registry registry=new EtcdRegistry();
        registry.init(registryConfig);
        log.info("registry init, config = {}",registryConfig);
    }

    //框架初始化
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置，单例模式（双重校验锁）
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
