package com.goznak;

import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class COMterminalApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(COMterminalApplication.class).headless(false).run(args);
    }
    @Bean("panelUpdater")
    PublishSubject<Boolean> panelUpdater(){
        return PublishSubject.create();
    }
    @Bean("receivedData")
    PublishSubject<String> receivedData(){
        return PublishSubject.create();
    }
}
