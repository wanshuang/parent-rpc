package com.gozap.demo.rpcservice.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ws
 * @date 2020/3/30
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private int port;
    private boolean stop;

    public MultiplexerTimeServer(int port) {
        this.port = port;
        initServer();
    }


    private void initServer() {
        if (port == 0) {
            System.out.println(String.format("port is invalid %d", port));
        }
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port), 1024);
            // 绑定等待连接的客户端个数，是个队列，
            // 当有新的客户端accept()的时候，就可以让新的连接队列放入到backlog队列中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println(String.format("server start in port:%d", port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {

            try {
                selector.select(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            //迭代
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                try {
                    //key进行操作
                    handleInput(key);
                } catch (IOException e) {
                    if (key != null) {
                        //异常择进行取消
                        key.cancel();
                        if (key.channel() != null) {
                            try {
                                key.channel().close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        //多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求信息
            if (key.isAcceptable()) {
                //Accept the new connection
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //Add the new connection to the selector
                sc.register(selector, SelectionKey.OP_READ);
            }
            //处理流信息
            if (key.isReadable()) {
                //Read the data
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                //如果有内容
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println(String.format("server receive order:%s", body));
                    //返回信息
                    String response = "server call time" + new Date(System.currentTimeMillis()).toString();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(response.getBytes().length);
                    //放入缓存
                    writeBuffer.put(response.getBytes());
                    writeBuffer.flip();
                    sc.write(writeBuffer);
                    if (!writeBuffer.hasRemaining()) {
                        System.out.println("server write succeed");
                    }

                } else if (readBytes < 0) {
                    //链路关闭
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

}
