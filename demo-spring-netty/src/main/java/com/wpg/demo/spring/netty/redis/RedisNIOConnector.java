package com.wpg.demo.spring.netty.redis;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import static java.nio.channels.SelectionKey.*;

/**
 * @author ChangWei Li
 * @version 2017-09-14 14:58
 */
@Slf4j
public class RedisNIOConnector {

    // 通道管理器
    private Selector selector;

    private void connect() {
        InetSocketAddress address = new InetSocketAddress("192.168.99.100", 6379);
        //打开Socket通道
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // 设置通道为非阻塞
            socketChannel.configureBlocking(false);
            // 连接服务器
            socketChannel.connect(address);
            // 获得通道管理器
            selector = Selector.open();
            // 将通道绑定到该通道管理器，并注册Connect事件
            socketChannel.register(selector, OP_CONNECT);
            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                if (selectionKey.isConnectable()) {
                    socketChannel.finishConnect();
                    socketChannel.register(selector, OP_WRITE);
                    break;
                } else if (selectionKey.isWritable()) {
                    String command = scanner.nextLine();

                    if ("exit".equals(command)) {
                        exit = true;
                    }

                    // server根据结束符判断当前命令结束，否则无法响应
                    command += "\r\n";

                    byte[] commadBytes = command.getBytes();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(commadBytes.length);
                    byteBuffer.put(commadBytes);

                    // 翻转缓冲区，否则读取不到数据
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);

                    socketChannel.register(selector, OP_READ);
                } else if (selectionKey.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);

                    byte[] msg = new byte[byteBuffer.position()];
                    byteBuffer.flip();
                    byteBuffer.get(msg);
                    log.info("receive message => {}", new String(msg));

                    socketChannel.register(selector, OP_READ | OP_WRITE);
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new RedisNIOConnector().connect();
    }

}
