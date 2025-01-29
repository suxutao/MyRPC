package com.mine.provider;

import com.mine.common.service.UserService;
import com.mine.myrpc.RpcApplication;
import com.mine.myrpc.config.RegistryConfig;
import com.mine.myrpc.config.RpcConfig;
import com.mine.myrpc.model.ServiceMetaInfo;
import com.mine.myrpc.registry.EtcdRegistry;
import com.mine.myrpc.registry.LocalRegistry;
import com.mine.myrpc.registry.Registry;
import com.mine.myrpc.server.HttpServer;
import com.mine.myrpc.server.VertxHttpServer;
import com.mine.myrpc.server.tcp.TcpServerHandler;
import com.mine.myrpc.server.tcp.VertxTcpServer;

//简易服务提供者实例
public class ProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        String serviceName=UserService.class.getName();
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry=new EtcdRegistry();
        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 启动Web服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
        //启动TCP服务
        VertxTcpServer vertxTcpServer=new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
