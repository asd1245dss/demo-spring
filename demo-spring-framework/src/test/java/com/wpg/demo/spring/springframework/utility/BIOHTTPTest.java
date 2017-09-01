package com.wpg.demo.spring.springframework.utility;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.servlet.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author ChangWei Li
 * @version 2017-08-30 13:27
 */
@Slf4j
public class BIOHTTPTest {

    @Test
    public void test_bio_server() throws IOException {
        new BIOHttpServer().init();
    }

    @Test
    public void test_bio_client() throws IOException {
        new BIOHttpClient().init();
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

    @Test
    public void print_jvm_classloader() {
        ClassLoader loader = BIOHTTPTest.class.getClassLoader();
        log.info("\ncurrent classloader: {}\nsecond classloader: {}\nthird classloader",
                loader, // AppClassLoader
                loader.getParent(), //ExtClassLoader
                loader.getParent().getParent()); // BootstrapClassloader but use c++ so would show null in java
    }

    @Test
    public void load_jar_dynamically() throws Exception {
        File file = new File("d:/c3p0-0.9.1.1.jar");
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);

        method.invoke(urlClassLoader, file.toURI().toURL());
        Class<?> cls = urlClassLoader.loadClass("com.mchange.util.Debug");
        log.info("\nload class {},\nfields: {}\nmethods: {}\nconstructors: {}", cls, cls.getDeclaredFields(), cls.getDeclaredMethods(), cls.getDeclaredConstructors());
        Constructor constructor = cls.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object object = constructor.newInstance();

        log.info("field {} = {}", cls.getFields()[1].getName(), cls.getFields()[1].get(object));
    }

}

@Slf4j
class PrimitiveServlet implements Servlet {

    private String servletName;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletName = this.getClass().getCanonicalName();

        log.info("init {} servlet", this.servletName);
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        log.info("service {} servlet", this.servletName);
        PrintWriter writer = response.getWriter();

        writer.println("Hello from " + this.servletName);
        writer.flush();
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        log.info("destroy {} servlet", this.servletName);
    }
}
