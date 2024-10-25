package com.lyf;

import com.lyf.Service.UserService;
import com.lyf.dao.User;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:28
 */
public class Consumer {
    public static void main(String[] args) {
        RpcApplication.init(); //包括读取rpc配置等
        MyProxyFactory myProxyFactory = new MyProxyFactory();
        UserService proxy = myProxyFactory.getProxy(UserService.class);
        User user = new User("小四","看美女");
        User user1 = proxy.getUser(user);
        System.out.println(user1.getName()+" "+user1.getHobby());
    }
}