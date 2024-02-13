package com.goznak;

import com.goznak.utils.Logger;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.apache.commons.logging.Log;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class COMterminalApplication {
    public static void main(String[] args) {
        Logger.info("Запускаю приложение");
        new SpringApplicationBuilder(COMterminalApplication.class).headless(false).run(args);
        Logger.info("Приложение запущено");
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
