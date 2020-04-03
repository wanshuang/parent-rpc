package com.gozap.demo.rpcservice.service;

import com.rpc.model.User;

/**
 * @author ws
 * @date 2020/3/20
 */
public interface UserService {
    User getUser(String account);
}
