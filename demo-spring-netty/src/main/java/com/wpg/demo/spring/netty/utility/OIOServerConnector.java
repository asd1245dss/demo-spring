package com.wpg.demo.spring.netty.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author ChangWei Li
 * @version 2017-09-09 21:05
 */
@Slf4j
public final class OIOServerConnector implements Connector {

    @Override
    public void handle(Socket socket) throws IOException {

        log.info("create connection with client: {}:{}", socket.getInetAddress().getHostAddress(), socket.getPort());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String request;

        while ((request = reader.readLine()) != null) {

            if ("end".equals(request)) {
                break;
            }

            log.info("receive message from client: {}", request);

            writer.write(request);
        }

        reader.close();
        writer.close();
        socket.close();
    }

}
