package com.gozap.demo.rpcservice.entity;

import lombok.Data;

/**
 * @author ws
 * @date 2020/3/20
 */
@Data
public class Response {

    private String requestId;//请求ID
    private int code;//返回码
    private String error_msg;//错误信息
    private Object data;//数据

}
