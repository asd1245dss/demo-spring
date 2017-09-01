package com.wpg.demo.spring.springframework.utility;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Data
@Slf4j
public class Response implements Closeable {

    private static final String WEB_ROOT = System.getProperty("user.dir");

    private Request request;

    private OutputStream outputStream;

    public Response(Request request, OutputStream outputStream) {
        this.request = request;
        this.outputStream = outputStream;
    }

    public void sendStaticResource() {
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
}
