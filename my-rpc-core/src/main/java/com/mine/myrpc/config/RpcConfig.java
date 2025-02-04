package com.mine.myrpc.config;

import com.mine.myrpc.loadbalancer.LoadBalancerKeys;
import lombok.Data;

//RPC框架配置类
@Data
public class RpcConfig {
    //名称
    private String name = "my-rpc";
    //版本号
    private String version = "1.0";
    //服务器主机名
    private String serverHost = "localhost";
    //服务器端口号
    private Integer serverPort = 8080;
    //mock服务
    private boolean mock = false;
    //注册中心配置
    private RegistryConfig registryConfig = new RegistryConfig();
    //负载均衡器
    private String loadBalancer= LoadBalancerKeys.CONSISTENT_HASH;
}
