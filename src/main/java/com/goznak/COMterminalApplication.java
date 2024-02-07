package com.goznak;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class COMterminalApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(COMterminalApplication.class).headless(false).run(args);
    }

}
