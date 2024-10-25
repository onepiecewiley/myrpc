package com.lyf.Service.Impl;

import com.lyf.Service.UserService;
import com.lyf.dao.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:31
 */
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        User newuser = new User("收到请求","祝你加油!");
        return newuser;
    }
}
