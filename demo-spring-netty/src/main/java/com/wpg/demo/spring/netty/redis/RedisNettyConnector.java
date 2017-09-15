package com.wpg.demo.spring.netty.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author ChangWei Li
 * @version 2017-09-15 14:43
 */
public class RedisNettyConnector {

    public static void main(String[] args) {

    }

}

class RedisProtocolDecoder extends ReplayingDecoder<Void> {

    private static final char CR = '\r';
    private static final char LF = '\n';

    private static final byte RETURN_NUM = '$';
    private static final byte ARG_NUM = '*';

    // 解码命令和参数
    private byte[][] cmds;

    // 参数个数
    private int arg;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (cmds == null) {
            if (in.readByte() == ARG_NUM) {
                devoceNumOfArgs(in);
            }
        } else {
            decodeArgs(in);
        }
        
    }

    private void decodeArgs(ByteBuf in) {
    }

    private void devoceNumOfArgs(ByteBuf in) {
        int numOfArgs = readInt(in);
    }

    private int readInt(ByteBuf in) {
        int nums = 0;
        char c;
        while ((c = (char) in.readByte()) != CR) {
            nums = (nums * 10) + (c - '0');
        }

        if (in.readByte() != LF) {
            throw new IllegalStateException("Invalid number");
        }

        return nums;
    }
}
