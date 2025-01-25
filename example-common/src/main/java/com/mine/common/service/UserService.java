package com.mine.common.service;

import com.mine.common.model.User;

public interface UserService {

    User getUser(User user);

    default short getNumber(){
        return 111;
    }
}
