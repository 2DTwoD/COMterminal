package com.goznak.visualization;

import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TerminalPanel extends JPanel {
    final
    MessageStructure messageStructure;
    final
    ApplicationContext context;
    final
    PublishSubject<Boolean> updater;
    JTextArea terminalTextArea = new JTextArea(10, 100);
    JPanel messageLine = new JPanel();
    public TerminalPanel(MessageStructure messageStructure, ApplicationContext context, PublishSubject<Boolean> updater) {
        super();
        this.updater = updater;
        this.messageStructure = messageStructure;
        this.context = context;
        messageLine.setLayout(new VerticalLayout(this, VerticalLayout.LEFT));
        JButton addMessagePartButton = new JButton("Добавить после");
        JLabel resultLabel = new JLabel("Результат");
        setLayout(new VerticalLayout(this, VerticalLayout.LEFT));
        addMessagePartButton.addActionListener(e -> {
            addMessagePart();
        });
        add(messageLine);
        add(addMessagePartButton);
        add(resultLabel);
        add(terminalTextArea);
        updater.onNext(true);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                resultLabel.setText(getFullMessage());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    public void addMessagePart(){
        getFullMessageBytes();
        MessagePart messagePart = new MessagePart();
        messageStructure.addPart(messagePart);
        updateMessageLine();
        updater.onNext(true);
    }
    public void addMessagePartBefore(MessagePart part){
        MessagePart newPart = new MessagePart();
        messageStructure.addPartBefore(part, newPart);

        updateMessageLine();
        updater.onNext(true);
    }
    public void removeMessagePart(MessagePart part){
        messageStructure.removePart(part);
        updateMessageLine();
        updater.onNext(true);
    }
    public void updateMessageLine(){
        messageLine.removeAll();
        for(MessagePart messagePart: messageStructure){
            MessagePartPanel messagePartPanel = context.getBean(MessagePartPanel.class);
            messagePartPanel.setMessagePart(messagePart);
            messageLine.add(messagePartPanel);
        }
    }
    public void saveParametersFromMessageLine(){
        List<MessagePart> newParts = new ArrayList<>();
        for(java.awt.Component c: messageLine.getComponents()){
            if(c instanceof MessagePartPanel){
                newParts.add(((MessagePartPanel) c).getMessagePart());
            }
        }
        messageStructure.setPartsList(newParts);
    }
    public void printInTerminal(String text){

    }

    public byte[] getFullMessageBytes(){
        Stream<Byte> resultStream = Stream.empty();
        for(java.awt.Component c: messageLine.getComponents()){
            if(c instanceof MessagePartPanel){
                Byte[] curArray = ArrayUtils.toObject(((MessagePartPanel) c).messagePart.HEX());
                resultStream = Stream.concat(resultStream, Stream.of(curArray));
            }
        }
        return ArrayUtils.toPrimitive(resultStream.toArray(Byte[]::new));
    }
    private String getFullMessage(){
        StringBuilder result = new StringBuilder();
        for(byte item: getFullMessageBytes()){
            result.append(String.format("%02x", item));
        }
        return result.toString();
    }
}
