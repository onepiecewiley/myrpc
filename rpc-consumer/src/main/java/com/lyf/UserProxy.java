/*
package com.lyf;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lyf.Service.UserService;
import com.lyf.dao.User;
import com.lyf.model.RpcRequest;
import com.lyf.model.RpcResponse;
import com.lyf.serializer.JdkSerializer;
import com.lyf.serializer.Serializer;

import java.io.IOException;

*/
/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 13:39
 *//*

public class UserProxy implements UserService {

    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = new RpcRequest(UserService.class.getName(),"getUser",new Class[]{User.class},new Object[]{user});
        try {
            byte[] bodyBytes = serializer.serializer(rpcRequest);
            System.out.println(serializer.deserializer(bodyBytes,RpcRequest.class));
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8000")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = (RpcResponse) serializer.deserializer(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
*/
