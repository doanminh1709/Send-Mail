package com.example.sendmail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SendMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendMailApplication.class, args);
    }

}
