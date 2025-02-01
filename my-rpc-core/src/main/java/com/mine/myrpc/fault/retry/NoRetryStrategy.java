package com.mine.myrpc.fault.retry;

import com.mine.myrpc.model.RpcResponse;

import java.util.concurrent.Callable;

//不进行重试
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
