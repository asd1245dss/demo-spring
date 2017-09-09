package com.wpg.demo.spring.netty.utility;

import java.io.IOException;
import java.net.Socket;

/**
 * @author ChangWei Li
 * @version 2017-09-09 21:34
 */
@FunctionalInterface
public interface Connector {

    void handle(Socket socket) throws IOException;

}
