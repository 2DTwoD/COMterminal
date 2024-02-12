package com.goznak.visualization;

import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        JLabel hexResultLabel = new JLabel("HEX:");
        JLabel textResultLabel = new JLabel("Текст:");
        setLayout(new VerticalLayout(this, VerticalLayout.LEFT));
        addMessagePartButton.addActionListener(e -> {
            addMessagePart();
        });
        add(messageLine);
        add(addMessagePartButton);
        add(hexResultLabel);
        add(textResultLabel);
        add(terminalTextArea);
        updater.onNext(true);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            try {
                hexResultLabel.setText(messageStructure.getFullMessageHEX());
                textResultLabel.setText(messageStructure.getFullMessage());
                for(java.awt.Component c: messageLine.getComponents()){
                    if(c instanceof MessagePartPanel){
                        ((MessagePartPanel)c).updatePanel();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    public void addMessagePart(){
        messageStructure.addPart();
        updateMessageLine();
        updater.onNext(true);
    }
    public void addMessagePartBefore(MessagePart part){
        messageStructure.addPartBefore(part);
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
    public void printInTerminal(String text){

    }
}
