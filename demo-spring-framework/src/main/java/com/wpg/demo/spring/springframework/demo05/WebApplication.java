package com.wpg.demo.spring.springframework.demo05;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author ChangWei Li
 * @version 2017-08-28 13:46
 */
@Configuration
@ComponentScan
@EnableWebMvc
@Slf4j
public class WebApplication {

    public static void main(String[] args) throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();

        tomcat.getHost().setAppBase(".");
        Context context = tomcat.addWebapp("/hello", new File("demo-spring-framework/src/main/resources/static/").getAbsolutePath());
        Tomcat.addServlet(context, "helloServlet", new HelloServlet());
        context.addServletMappingDecoded("/test", "helloServlet");


        Connector connector = new Connector("org.apache.coyote.http11.Http11Nio2Protocol");
        connector.setPort(8181);
        tomcat.getService().addConnector(connector);

        tomcat.start();

        tomcat.getServer().await();
    }

}

@Slf4j
@WebServlet(urlPatterns = "/hello/test")
class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 3260403102794833832L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("catch request {}", this);
        resp.getWriter().println("{\"message\":\"Success\"}");
        resp.setHeader("Content-Type", "application/json");
        resp.setHeader("Server", "Embedded-Tomcat");
        resp.setStatus(200);
    }
}
