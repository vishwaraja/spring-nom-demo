package com.nominum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Throwable {
        System.setProperty("server.tomcat.max-threads", "200");
        System.setProperty("server.connection-timeout", "60000");
        SpringApplication.run(Application.class, args);
    }



}
