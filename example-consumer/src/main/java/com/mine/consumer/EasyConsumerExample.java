package com.mine.consumer;


import com.mine.common.model.User;
import com.mine.common.service.UserService;
import com.mine.myrpc.config.RpcConfig;
import com.mine.myrpc.proxy.ServiceProxy;
import com.mine.myrpc.proxy.ServiceProxyFactory;
import com.mine.myrpc.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        // 获取 UserService 的实现类
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("asu");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);

        // 调用
        for (int i = 2; i <= 1000; i++) {
            User user2 = new User();
            user2.setName("asu"+i);
            User newUser2 = userService.getUser(user2);
            if (newUser2 != null) {
                System.out.println(newUser2.getName());
            } else {
                System.out.printf("user%d == null\n",i);
            }
        }

    }
}
