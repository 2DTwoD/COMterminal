package com.goznak.visualization;

import com.goznak.types.Func;
import com.goznak.types.MessagePart;
import com.goznak.types.SimpleDocumentListener;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Scope("prototype")
public class MessagePartPanel extends JPanel {
    final
    PublishSubject<Boolean> updater;
    final
    TerminalPanel terminalPanel;
    MessagePart messagePart;
    JComboBox<Func> funcComboBox = new JComboBox<>(Func.values());
    LabelWithNumberTextField numOfBytesTextField = new LabelWithNumberTextField("Кол-во байт:", 2);
    LabelWithTextField messageTextField = new LabelWithTextField("Знач.:", 10);
    JButton deleteButton = new JButton("Убрать");
    JButton addBeforeButton = new JButton("Добавить до");
    @Autowired
    public MessagePartPanel(PublishSubject<Boolean> updater, TerminalPanel terminalPanel) {
        super();
        this.updater = updater;
        this.terminalPanel = terminalPanel;
        funcComboBox.addActionListener(e -> messagePart.setFunc((Func) funcComboBox.getSelectedItem()));
        numOfBytesTextField.getDocument().addDocumentListener(
                (SimpleDocumentListener) e -> messagePart.setNumOfBytes(Integer.parseInt(numOfBytesTextField.getText()))
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
        add(numOfBytesTextField);
        add(messageTextField);
        add(deleteButton);
        add(addBeforeButton);
        updater.onNext(true);
    }
    public void setMessagePart(MessagePart messagePart){
        if(messagePart == null) return;
        this.messagePart = messagePart;
        funcComboBox.setSelectedItem(messagePart.getFunc());
        numOfBytesTextField.setText(String.valueOf(messagePart.getNumOfBytes()));
        messageTextField.setText(messagePart.getValue());
    }
}
