package com.wpg.demo.spring.springframework.utility;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
        ServerSocket serverSocket = new ServerSocket(8181, 2, InetAddress.getByName("0.0.0.0"));
        final boolean[] stop = {true};

        while (stop[0]) {
            Socket socket = serverSocket.accept();
            executorService.execute(() -> {
                try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    log.info("new client {} has connected to the server", socket.getInetAddress().getHostAddress());
                    String tmp;
                    while ((tmp = reader.readLine()) != null) {
                        if ("shutdown".equals(tmp)) {
                            stop[0] = false;
                            log.info("receive shutdown command");
                            break;
                        }
                        log.info("{} receive => {}", socket, tmp);
                    }

                } catch (Exception ignore) {

                }

            });
        }

        executorService.shutdown();
        log.info("server has been stop successfully !");
    }

    @Test
    public void test_bio_client() throws IOException {
        Socket socket = new Socket("www.baidu.com", 80);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        printWriter.println("GET / HTTP/1.1");
        printWriter.println("Host: www.baidu.com");
        printWriter.println("Accept-Encoding: gzip");
        printWriter.println("Connection: Close");
        printWriter.println();

        String msg;

        while ((msg = reader.readLine()) != null) {
            log.info(msg);
        }

        reader.close();
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

}
