package com.ote.file.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class FileServiceRunner {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceRunner.class, args);
    }
}