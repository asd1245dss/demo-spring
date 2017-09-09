package com.wpg.demo.spring.netty.utility;

import lombok.Data;

/**
 * @author ChangWei Li
 * @version 2017-09-09 21:37
 */
public abstract class ServerAdapter {

    static final int DEFAULT_SERVER_PORT = 80;

    Integer serverPort;

    public abstract void init();
}
