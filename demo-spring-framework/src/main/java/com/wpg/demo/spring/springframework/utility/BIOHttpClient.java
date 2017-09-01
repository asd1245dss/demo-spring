package com.wpg.demo.spring.springframework.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author ChangWei Li
 * @version 2017-09-01 13:53
 */
@Slf4j
public class BIOHttpClient {

    public void init() throws IOException {
        long start = System.currentTimeMillis();
        Socket socket = new Socket("avatar.csdn.net", 80);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        String requestContent = "GET /B/0/E/1_yankai0219.jpg HTTP/1.1" +
                "\r\n" +
                "Host: avatar.csdn.net" +
                "\r\n" +
                "Referer: http://blog.csdn.net/yankai0219/article/details/8269922" +
                "\r\n" +
                "Connection: closed" +
                "\r\n";

        printWriter.println(requestContent);

        log.info("\nelapsed time: {}\ncontent: \n{}", System.currentTimeMillis() - start, Request.readAll(socket.getInputStream()));

        printWriter.close();
        socket.close();
    }

}
