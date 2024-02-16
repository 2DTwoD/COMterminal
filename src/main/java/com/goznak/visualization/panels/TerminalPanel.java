package com.goznak.visualization.panels;

import com.goznak.communication.Connection;
import com.goznak.message.MessagePart;
import com.goznak.message.MessageStructure;
import com.goznak.utils.Logger;
import com.goznak.visualization.components.DynamicLabelWithSubscribe;
import com.goznak.visualization.components.ScrolledTextArea;
import com.goznak.visualization.components.VerticalLayout;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TerminalPanel extends JPanel {
    final
    MessageStructure messageStructure;
    final
    ApplicationContext context;
    final
    PublishSubject<Boolean> panelUpdater;
    final
    PublishSubject<String> receivedData;
    final
    Connection connection;
    ScrolledTextArea terminalTextArea = new ScrolledTextArea(20, 100);
    JPanel messageLinePanel = new JPanel();
    public TerminalPanel(MessageStructure messageStructure, ApplicationContext context, PublishSubject<Boolean> panelUpdater,
                         PublishSubject<String> receivedData, Connection connection) {
        super();
        this.panelUpdater = panelUpdater;
        this.messageStructure = messageStructure;
        this.context = context;
        this.receivedData = receivedData;
        this.connection = connection;
        setLayout(new VerticalLayout(this, VerticalLayout.CENTER, 10));
        messageLinePanel.setLayout(new VerticalLayout(this, VerticalLayout.LEFT));

        JButton addMessagePartButton = new JButton("Добавить после");
        DynamicLabelWithSubscribe hexResultLabel = new DynamicLabelWithSubscribe("Результат ввода:");
        hexResultLabel.setLabelFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        JButton sendButton = new JButton(">>Отправить<<");
        sendButton.setPreferredSize(new Dimension(200, 30));

        addMessagePartButton.addActionListener(e -> addMessagePart());
        sendButton.addActionListener(e -> {
            connection.writeData(messageStructure.getFullMessageBytes());
            printRequest();
        });

        add(messageLinePanel);
        add(addMessagePartButton);
        add(hexResultLabel);
        add(sendButton);
        add(terminalTextArea);

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                hexResultLabel.setText(String.format("%s (HEX: %s)",
                        messageStructure.getFullMessage(),
                        messageStructure.getFullMessageHEX()));

                for(java.awt.Component c: messageLinePanel.getComponents()){
                    if(c instanceof MessagePartPanel){
                        ((MessagePartPanel)c).updatePanel();
                    }
                }
                sendButton.setEnabled(connection.portOpened());
            }
            catch (Exception e){
                Logger.error("Ошибка в цикле обновления компонентов", e);
            }
        }, 0, 1, TimeUnit.SECONDS);
        receivedData.subscribe(this::printResponse);
        panelUpdater.onNext(true);
    }
    public void addMessagePart(){
        messageStructure.addPart();
        updateMessageLine();
        panelUpdater.onNext(true);
    }
    public void addMessagePartBefore(MessagePart part){
        messageStructure.addPartBefore(part);
        updateMessageLine();
        panelUpdater.onNext(true);
    }
    public void removeMessagePart(MessagePart part){
        messageStructure.removePart(part);
        updateMessageLine();
        panelUpdater.onNext(true);
    }
    public void updateMessageLine(){
        messageLinePanel.removeAll();
        for(MessagePart messagePart: messageStructure){
            MessagePartPanel messagePartPanel = context.getBean(MessagePartPanel.class);
            messagePartPanel.setMessagePart(messagePart);
            messagePartPanel.updatePanel();
            messageLinePanel.add(messagePartPanel);
        }
    }
    public void printRequest(){
        try {
            terminalTextArea.insertString(
                    String.format("%s >>Запрос>> %s (HEX: %s)\n", getDateString(),
                            messageStructure.getFullMessage(),
                            messageStructure.getFullMessageHEX()));

        }
        catch (Exception e){
            Logger.error("Ошибка вставки текста в поле", e);
        }
    }
    public void printResponse(String text){
        try{
            terminalTextArea.insertString(String.format("%s <<Ответ<< %s\n", getDateString(),
                    text.chars().mapToObj(i -> {
                        if(i < 32){
                            return String.format(" %s ", i);
                        } else {
                            return (char) i;
                        }
                    }).map(Objects::toString).collect(Collectors.joining())));
        }
        catch (Exception e){
            Logger.error("Ошибка вставки текста в поле", e);
        }
    }
    private String getDateString(){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
    }
    public String getTextAreaContent(){
        return terminalTextArea.getText();
    }
}
