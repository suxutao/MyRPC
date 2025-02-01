package com.mine.myrpc.retry.fault;

import com.mine.myrpc.fault.retry.FixedIntervalRetryStrategy;
import com.mine.myrpc.fault.retry.NoRetryStrategy;
import com.mine.myrpc.fault.retry.RetryStrategy;
import com.mine.myrpc.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {
    RetryStrategy retryStrategy=new FixedIntervalRetryStrategy();

    @Test
    public void doRetry(){
        try {
            RpcResponse rpcResponse=retryStrategy.doRetry(()->{
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
