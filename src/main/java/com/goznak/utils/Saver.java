package com.goznak.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goznak.communication.ComParameters;
import com.goznak.message.MessagePart;
import com.goznak.message.MessageStructure;
import com.goznak.visualization.panels.TerminalPanel;
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
    final
    TerminalPanel terminalPanel;

    private final String serialSettingsFileName;
    private final String messageStructureFileName;
    private final String parametersDirectoryName;
    private final String historyFileName;
    public Saver(ComParameters comParameters, MessageStructure messageStructure,
                 TerminalPanel terminalPanel, @Value("${serial.settings.file.name}") String serialSettingsFileName,
                 @Value("${message.structure.file.name}") String messageStructureFileName,
                 @Value("${history.file.name}") String historyFileName,
                 @Value("${parameters.directory.name}") String parametersDirectoryName) {
        this.terminalPanel = terminalPanel;
        this.messageStructureFileName = messageStructureFileName;
        this.messageStructure = messageStructure;
        this.serialSettingsFileName = serialSettingsFileName;
        this.comParameters = comParameters;
        this.parametersDirectoryName = parametersDirectoryName;
        this.historyFileName = historyFileName;
        ObjectMapper mapper = new ObjectMapper();
        Logger.info("Загружаю настройки из файлов");
        try {
            ComParameters parsFromFile = mapper.readValue(new File(parametersDirectoryName + serialSettingsFileName), ComParameters.class);
            this.comParameters.setNewParameters(parsFromFile);
            MessagePart[] messageStructuresList = mapper.readValue(new File(parametersDirectoryName + messageStructureFileName), MessagePart[].class);
            this.messageStructure.setNewPartsList(Arrays.stream(messageStructuresList).collect(Collectors.toCollection(ArrayList::new)));
            Logger.info("Настройки загружены");
        } catch (IOException e) {
            Logger.error("Ошибка чтения файла", e);
        }
    }
    public void save(){
        Logger.info("Сохраняю настройки в файлах");
        File dir = new File(parametersDirectoryName);
        if (!dir.exists()){
            dir.mkdirs();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(parametersDirectoryName + historyFileName, true)){
            objectMapper.writeValue(new File(parametersDirectoryName + serialSettingsFileName), comParameters);
            objectMapper.writeValue(new File(parametersDirectoryName + messageStructureFileName), messageStructure.getPartsList());
            writer.write(terminalPanel.getTextAreaContent());
            writer.flush();
            Logger.info("Настройки сохранены");
        } catch (IOException e) {
            Logger.error("Ошибка записи файла", e);
        }
    }
}
