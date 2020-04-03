package com.gozap.demo.rpcservice.service.impl;

import com.gozap.demo.rpcservice.anno.RpcService;
import com.gozap.demo.rpcservice.service.UserService;
import com.rpc.model.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ws
 * @date 2020/3/20
 */
@RpcService
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(String account) {
        return null;
    }
}
