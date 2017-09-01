package com.wpg.demo.spring.springframework.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.*;

@Slf4j
public class Response extends ResponseAdapter implements Closeable {

    private static final String WEB_ROOT = System.getProperty("user.dir");

    @Getter
    @Setter
    private Request request;

    private OutputStream outputStream;

    Response(Request request, OutputStream outputStream) {
        this.request = request;
        this.outputStream = outputStream;
    }

    void sendStaticResource() {
        try {
            //content-length could determine the browser receive the reponse
            String message = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html;charset=utf-8\r\n" +
                    "Connection: Keep-Alive\r\n" +
                    "Server: BIO_JAVA/1.1\r\n" +
                    "X-Ua-Compatible: IE=Edge,chrome=1\r\n" +
                    "Set-Cookie: BDSVRTM=131; path=/\r\n" +
                    "\r\n";
            File file = new File(WEB_ROOT, "target/classes" + request.getUri());
            if (file.isFile() && file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                message += Request.readAll(inputStream);
            } else {
                message = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html;charset=utf-8\r\n" +
                        "Content-Length: 23\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "Server: BIO_JAVA/1.1\r\n" +
                        "X-Ua-Compatible: IE=Edge,chrome=1\r\n" +
                        "Set-Cookie: BDSVRTM=131; path=/\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
            }

            outputStream.write(message.getBytes());

            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null)
            outputStream.close();
        log.info("socket closed successfully !");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outputStream, true);
    }
}
