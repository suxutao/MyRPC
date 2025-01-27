package com.mine.myrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
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

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发请求
        String serviceName=method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //从注册中心获取服务器请求地址
            RpcConfig rpcConfig= RpcApplication.getRpcConfig();
            Registry registry=new EtcdRegistry();
            ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList=registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            //todo 取第一个地址
            ServiceMetaInfo selectedServiceMetaInfo=serviceMetaInfoList.get(0);

            //发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
