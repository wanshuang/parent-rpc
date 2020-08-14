package com.gozap.demo.rpcservice.socket.bio;

import java.io.*;
import java.net.Socket;

/**
 * @author ws
 * @date 2020/3/27
 */
public class SocketClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }

        Socket socket = null;
        BufferedReader in = null;
        OutputStream out = null;

        try {
            socket = new Socket("127.0.0.1", port);
            //设置超时时间
            socket.setSoTimeout(10000);
            out = socket.getOutputStream();
            String message = "你好  server";
            socket.getOutputStream().write(message.getBytes("UTF-8"));
            socket.shutdownOutput();
            System.out.println("Send order 2 server succeed.");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String resp = in.readLine();
            System.out.println("Now is:" + resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
