package com.wpg.demo.spring.netty.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ChangWei Li
 * @version 2017-09-09 21:36
 */
@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OIOServer extends ServerAdapter implements Server {

    private Connector connector;

    private OIOServer(Connector connector, Integer serverPort) {
        super();
        this.serverPort = serverPort;
        this.connector = connector;
    }

    @Override
    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(super.serverPort)) {
            Socket socket = serverSocket.accept();
            connector.handle(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int serverPort = DEFAULT_SERVER_PORT;

        if (args.length > 0) {
            serverPort = Integer.parseInt(args[0]);
        }

        new OIOServer(new OIOServerConnector(), serverPort).init();
    }
}
