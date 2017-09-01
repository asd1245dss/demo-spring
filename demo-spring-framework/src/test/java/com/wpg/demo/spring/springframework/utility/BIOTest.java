package com.wpg.demo.spring.springframework.utility;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author ChangWei Li
 * @version 2017-08-30 13:27
 */
@Slf4j
public class BIOTest {

    @Test
    public void test_bio_server() throws IOException {
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

                    log.info("{} receive => \n{}", socket, request.getMsg());

                } catch (Exception ignore) {

                }

            });
        }

        executorService.shutdown();
        log.info("server has been stop successfully !");
    }

    @Test
    public void test_bio_client() throws IOException {
        long start = System.currentTimeMillis();
        Socket socket = new Socket("avatar.csdn.net", 80);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        String requestContent = "GET /B/0/E/1_yankai0219.jpg HTTP/1.1" +
                "\r\n" +
                "Host: avatar.csdn.net" +
                "\r\n" +
                "Referer: http://blog.csdn.net/yankai0219/article/details/8269922" +
                "\r\n" +
                "Connection: closed" +
                "\r\n";

        printWriter.println(requestContent);

        log.info("\nelapsed time: {}\ncontent: \n{}", System.currentTimeMillis() - start, Request.readAll(socket.getInputStream()));

        printWriter.close();
        socket.close();

    }

    @Test
    public void test_gzip_compress_to_file() throws IOException {

        FileOutputStream outputStream = new FileOutputStream("d:/gzip.gz");
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        String message = "Hello GZIP";
        gzipOutputStream.write(message.getBytes());

        gzipOutputStream.finish();
        gzipOutputStream.flush();
        gzipOutputStream.close();
        outputStream.close();
    }

    @Test
    public void test_gzip_compress_and_decompress_in_memory() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        String message = "Hello GZIP";

        // should flush and finish first or close immediatelly otherwise, you can't read all of the byte data
        gzipOutputStream.write(message.getBytes());
        gzipOutputStream.finish();
        gzipOutputStream.flush();

        byte[] gzipData = outputStream.toByteArray();

        log.info("\n write data: {} \n gzip data: {}", message.getBytes(), gzipData);

        InputStream bais = new ByteArrayInputStream(gzipData);
        outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = new GZIPInputStream(bais);

        byte[] buffer = new byte[1024];
        int length = gzipInputStream.read(buffer);
        outputStream.write(buffer);

        log.info("read data length: {} detail: {}", length, new String(outputStream.toByteArray()));

        outputStream.close();
    }

    @Test
    public void test_gzip_decompress_from_file() throws IOException {
        FileInputStream inputStream = new FileInputStream("d:/gzip.gz");
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        byte[] buffer = new byte[10];
        int readPositon = gzipInputStream.read(buffer);
        log.info("read content size: {} detail: {}", readPositon, new String(buffer));

        inputStream.close();
        gzipInputStream.close();
    }

    @Test
    public void read_env_user_dir() {
        log.info("user.dir => {}", System.getProperty("user.dir"));
    }

    @Test
    public void split_request_content() {
        String request = "GET /api/anime/1 HTTP/1.1" +
                "\r\n" +
                "Host: jikan.me" +
                "\r\n" +
                "Connection: closed" +
                "\r\n";

        log.info("{}", Arrays.toString(request.split("\r\n")));
    }

}

@Data
@Slf4j
class Request implements Closeable {

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

@Data
@Slf4j
class Response implements Closeable {

    private static final String WEB_ROOT = System.getProperty("user.dir");

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
}
