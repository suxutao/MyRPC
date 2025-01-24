package com.mine.consumer;

import com.mine.myrpc.config.RpcConfig;
import com.mine.myrpc.utils.ConfigUtils;

public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpcConfig= ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
