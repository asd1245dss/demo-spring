package com.wpg.demo.spring.springframework.utility;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ChangWei Li
 * @version 2017-09-01 13:52
 */
@Slf4j
class BIOHttpServer {

    void init() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(8181, 1);

        final boolean[] stop = {true};

        while (stop[0]) {
            Socket socket = serverSocket.accept();
            executorService.execute(() -> {

                try (
                    Request request = new Request(socket.getInputStream());
                    Response response = new Response(request, socket.getOutputStream())
                ) {
                    log.info("new client {} has connected to the server", socket.getInetAddress().getHostAddress());

                    request.parse();
                    response.sendStaticResource();

                    if ("/shutdown".equals(request.getUri())) {
                        stop[0] = false;
                    }

                    log.info("{} receive => \n{}", socket, request.getMsg());

                } catch (Exception ignore) {

                }

            });
        }

        executorService.shutdown();
        log.info("server has been stop successfully !");
    }

}
