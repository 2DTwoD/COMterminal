package com.goznak.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goznak.communication.ComParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Saver {
    final
    ComParameters comParameters;

    private final String serialSettingsPath;
    public Saver(ComParameters comParameters, @Value("${serial.settings.path}") String serialSettingsPath) {
        this.serialSettingsPath = serialSettingsPath;
        this.comParameters = comParameters;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ComParameters parsFromFile = mapper.readValue(new File(serialSettingsPath), ComParameters.class);
            this.comParameters.setNewParameters(parsFromFile);
        } catch (IOException e) {
            System.out.println("Проблемы при чтении настроек последовательного порта");
        }
    }
    public void save(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(serialSettingsPath), comParameters);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
