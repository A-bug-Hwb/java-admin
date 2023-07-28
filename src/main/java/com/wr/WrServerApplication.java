package com.wr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WrServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrServerApplication.class, args);
        System.out.println("启动成功");
    }

}
