package com.calvin.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.calvin.*"})
public class CalvinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalvinApplication.class, args);
    }




}
