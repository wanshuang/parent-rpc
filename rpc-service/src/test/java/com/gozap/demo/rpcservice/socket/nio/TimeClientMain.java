package com.gozap.demo.rpcservice.socket.nio;

/**
 * @author ws
 * @date 2020/3/31
 */
public class TimeClientMain {

    public static void main(String args[]){
        int port = 8080;

        TimeClientHandle client = new TimeClientHandle("127.0.0.1",port);
        new Thread(client, "TimeClient-001").start();
    }
}
