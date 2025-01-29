package com.mine.myrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.mine.myrpc.RpcApplication;
import com.mine.myrpc.config.RpcConfig;
import com.mine.myrpc.constant.RpcConstant;
import com.mine.myrpc.model.RpcRequest;
import com.mine.myrpc.model.RpcResponse;
import com.mine.myrpc.model.ServiceMetaInfo;
import com.mine.myrpc.registry.EtcdRegistry;
import com.mine.myrpc.registry.Registry;
import com.mine.myrpc.serializer.JdkSerializer;
import com.mine.myrpc.serializer.Serializer;
import com.mine.myrpc.server.tcp.VertxTcpClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {

    //调用代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = new EtcdRegistry();
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            //发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest,selectedServiceMetaInfo);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
