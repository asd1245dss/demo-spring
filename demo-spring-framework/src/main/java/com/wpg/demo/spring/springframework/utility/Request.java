package com.wpg.demo.spring.springframework.utility;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Data
@Slf4j
public class Request implements Closeable {

    private InputStream inputStream;

    private String msg;

    private String uri;

    private String header;

    private String content;

    Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    static String readAll(InputStream inputStream) {
        StringBuilder msg = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            boolean ready = false;

            while (true) {
                if (reader.ready()) {
                    ready = true;
                    int tmp = reader.read();

                    msg.append((char) tmp);
                } else {
                    if (ready) {
                        break;
                    }
                }
            }

        } catch (Exception ignore) {

        }

        return msg.toString();
    }

    void parse() {
        this.msg = readAll(inputStream);

        if (msg.length() > 0) {
            int headerIndex = msg.indexOf("\r\n\r\n");
            int uriStartIndex = msg.indexOf(" ");
            int uriEndIndex = msg.indexOf(" ", uriStartIndex + 1);
            this.uri = msg.substring(uriStartIndex + 1, uriEndIndex);
            this.header = msg.substring(uriEndIndex + 1, headerIndex);
            this.content = msg.substring(headerIndex + 4);
        }
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null)
            inputStream.close();
        log.info("socket closed successfully !");
    }
}