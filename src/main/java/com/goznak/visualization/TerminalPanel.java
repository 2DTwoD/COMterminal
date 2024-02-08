package com.goznak.visualization;

import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import com.goznak.types.NumberTextField;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

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
        JButton addMessagePartButton = new JButton("Добавить часть");
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
    }
    public void addMessagePart(){
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
    private void updateMessageLine(){
        messageLine.removeAll();
        for(MessagePart messagePart: messageStructure){
            MessagePartPanel messagePartPanel = context.getBean(MessagePartPanel.class);
            messagePartPanel.setMessagePart(messagePart);
            messageLine.add(messagePartPanel);
        }
    }
}
