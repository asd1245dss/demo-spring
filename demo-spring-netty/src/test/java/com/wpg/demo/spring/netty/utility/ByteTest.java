package com.wpg.demo.spring.netty.utility;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author ChangWei Li
 * @version 2017-09-15 15:38
 */
public class ByteTest {

    @Test
    public void test_byte_to_char() {
        // Java中 byte为一个字节，char为两个字节
        byte a = 53;
        int c = 53;
        System.out.println((char) a);
        System.out.println((char) c);
        System.out.println((int) a);
        System.out.println((a - '0'));
    }

    @Test
    public void test_read_byte_array_line() throws UnsupportedEncodingException {
        byte lf = '\r';
        byte[] msg = new byte[] {49, 52, 53, 37, '\r', '\n', 49, 52, 53, 37, '\r', '\n'};
        String msgStr = new String(msg, "UTF-8");
        System.out.println(msgStr);
        System.out.println(msgStr.indexOf('\r'));
    }

}
