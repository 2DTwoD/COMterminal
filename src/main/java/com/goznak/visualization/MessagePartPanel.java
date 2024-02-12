package com.goznak.visualization;

import com.goznak.types.Func;
import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import com.goznak.types.SimpleDocumentListener;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class MessagePartPanel extends JPanel {
    final
    PublishSubject<Boolean> updater;
    final
    TerminalPanel terminalPanel;
    final
    MessageStructure  messageStructure;
    MessagePart messagePart;
    JComboBox<Func> funcComboBox = new JComboBox<>(Func.funcsArray);
    JComboBox<Integer> numOfBytesComboBox = new JComboBox<>(numOfBytesArray);
    JLabel numOfBytesLabel = new JLabel();
    LabelWithTextField messageTextField = new LabelWithTextField("Знач.:", 10);
    JButton deleteButton = new JButton("Убрать");
    JButton addBeforeButton = new JButton("Добавить до");
    static final Integer[] numOfBytesArray = new Integer[]{1, 2, 4, 8};
    @Autowired
    public MessagePartPanel(PublishSubject<Boolean> updater, TerminalPanel terminalPanel, MessageStructure messageStructure) {
        super();
        this.updater = updater;
        this.terminalPanel = terminalPanel;
        this.messageStructure = messageStructure;
        funcComboBox.addActionListener(e ->
                messagePart.setFunc(funcComboBox.getItemAt(funcComboBox.getSelectedIndex()))
        );
        numOfBytesComboBox.addActionListener(e ->
                messagePart.setNumOfBytes(numOfBytesComboBox.getItemAt(numOfBytesComboBox.getSelectedIndex()))
        );
        messageTextField.getDocument().addDocumentListener(
                (SimpleDocumentListener) e -> messagePart.setValue(messageTextField.getText())
        );
        deleteButton.addActionListener(e -> {
            if(messagePart == null) return;
            terminalPanel.removeMessagePart(messagePart);
        });
        addBeforeButton.addActionListener(e -> {
            if(messagePart == null) return;
            terminalPanel.addMessagePartBefore(messagePart);
        });

        add(funcComboBox);
        add(messageTextField);
        add(new JLabel("Занимает байт:"));
        add(numOfBytesLabel);
        add(numOfBytesComboBox);
        add(deleteButton);
        add(addBeforeButton);
        updater.onNext(true);
    }
    public void setMessagePart(MessagePart messagePart){
        if(messagePart == null) return;
        this.messagePart = messagePart;
        if(Arrays.asList(Func.funcsArray).contains(messagePart.getFunc())) {
            funcComboBox.setSelectedItem(messagePart.getFunc());
        }
        if(Arrays.asList(numOfBytesArray).contains(messagePart.getNumOfBytes())) {
            numOfBytesComboBox.setSelectedItem(messagePart.getNumOfBytes());
        }
        messageTextField.setText(messagePart.getValue());
    }
    public void updatePanel(){
        if(messagePart == null) return;
        funcComboBox.setSelectedItem(messagePart.getFunc());
        switch(messagePart.getFunc().value()){
            case DECIMAL -> {
                messageTextField.setEnabled(true);
                numOfBytesComboBox.setVisible(true);
                numOfBytesComboBox.setSelectedItem(messagePart.getNumOfBytes());
                numOfBytesLabel.setVisible(false);
            }
            case NUMBER_OF_BYTES -> {
                messageTextField.setEnabled(false);
                messageTextField.setText(String.valueOf(messageStructure.getNumOfBytes()));
                numOfBytesComboBox.setVisible(false);
                numOfBytesLabel.setVisible(true);
                numOfBytesLabel.setText(String.valueOf(messagePart.getNumOfBytes()));
            }
            case CHECK_SUM -> {
                messageTextField.setEnabled(false);
                messageTextField.setText(String.valueOf(messageStructure.getCheckSum()));
                numOfBytesComboBox.setVisible(false);
                numOfBytesLabel.setVisible(true);
                numOfBytesLabel.setText(String.valueOf(messagePart.getNumOfBytes()));
            }
            default -> {
                messageTextField.setEnabled(true);
                numOfBytesComboBox.setVisible(false);
                numOfBytesLabel.setVisible(true);
                numOfBytesLabel.setText(String.valueOf(messagePart.getNumOfBytes()));
            }
        }
    }
}
