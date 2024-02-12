package com.goznak.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goznak.communication.ComParameters;
import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class Saver {
    final
    ComParameters comParameters;
    final
    MessageStructure messageStructure;

    private final String serialSettingsName;
    private final String messageStructureName;

    private final String parametersDirectoryName;
    public Saver(ComParameters comParameters, MessageStructure messageStructure,
                 @Value("${serial.settings.name}") String serialSettingsName,
                 @Value("${message.structure.name}") String messageStructureName,
                 @Value("${parameters.directory.name}") String parametersDirectoryName) {
        this.messageStructureName = messageStructureName;
        this.messageStructure = messageStructure;
        this.serialSettingsName = serialSettingsName;
        this.comParameters = comParameters;
        this.parametersDirectoryName = parametersDirectoryName;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ComParameters parsFromFile = mapper.readValue(new File(parametersDirectoryName + serialSettingsName), ComParameters.class);
            this.comParameters.setNewParameters(parsFromFile);
            MessagePart[] messageStructuresList = mapper.readValue(new File(parametersDirectoryName + messageStructureName), MessagePart[].class);
            this.messageStructure.setNewPartsList(Arrays.stream(messageStructuresList).collect(Collectors.toCollection(ArrayList::new)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void save(){
        File dir = new File(parametersDirectoryName);
        if (!dir.exists()){
            dir.mkdirs();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(parametersDirectoryName + serialSettingsName), comParameters);
            objectMapper.writeValue(new File(parametersDirectoryName + messageStructureName), messageStructure.getPartsList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
