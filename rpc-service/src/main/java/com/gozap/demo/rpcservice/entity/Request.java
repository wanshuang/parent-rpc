package com.gozap.demo.rpcservice.entity;

import lombok.Data;

/**
 * @author ws
 * @date 2020/3/20
 */
@Data
public class Request {

    private String id;
    private String className;// 类名
    private String methodName;// 函数名称
    private Class<?>[] parameterTypes;// 参数类型
    private Object[] parameters;// 参数列表

}
