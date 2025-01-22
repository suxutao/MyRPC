package com.mine.provider;


import com.mine.common.service.UserService;
import com.mine.myrpc.registry.LocalRegistry;
import com.mine.myrpc.server.HttpServer;
import com.mine.myrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        // 启动Web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8081);
    }
}
