package com.goznak.visualization.panels;

import com.goznak.types.Func;
import com.goznak.message.MessagePart;
import com.goznak.message.MessageStructure;
import com.goznak.types.SimpleDocumentListener;
import com.goznak.visualization.components.LabelWithTextField;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

@Component
@Scope("prototype")
public class MessagePartPanel extends JPanel {
    final
    PublishSubject<Boolean> panelUpdater;
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
    public MessagePartPanel(PublishSubject<Boolean> panelUpdater, TerminalPanel terminalPanel, MessageStructure messageStructure) {
        super();
        this.panelUpdater = panelUpdater;
        this.terminalPanel = terminalPanel;
        this.messageStructure = messageStructure;
        numOfBytesLabel.setPreferredSize(new Dimension(50,20));
        numOfBytesComboBox.setPreferredSize(new Dimension(50,20));
        funcComboBox.addActionListener(e -> messagePart.setFunc(funcComboBox.getItemAt(funcComboBox.getSelectedIndex())));
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
        panelUpdater.onNext(true);
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
        switch(messagePart.getFunc().value()){
            case DECIMAL -> {
                messagePart.setNumOfBytes(numOfBytesComboBox.getItemAt(numOfBytesComboBox.getSelectedIndex()));
                messageTextField.setEnabled(true);
                numOfBytesComboBox.setVisible(true);
                numOfBytesLabel.setVisible(false);
            }
            case NUMBER_OF_BYTES -> {
                messageTextField.setText(String.valueOf(messageStructure.getNumOfBytes()));
                updatePanelForCsNob();
            }
            case CHECK_SUM_XOR -> {
                messageTextField.setText(String.valueOf(messageStructure.getCheckSumXor()));
                updatePanelForCsNob();
            }
            case CHECK_SUM_CRC16_CCITT -> {
                messageTextField.setText(String.valueOf(messageStructure.getCheckSumCrcCcit()));
                updatePanelForCsNob();
            }
            default -> {
                numOfBytesLabel.setText(String.valueOf(messagePart.getNumOfBytes()));
                messageTextField.setEnabled(true);
                numOfBytesComboBox.setVisible(false);
                numOfBytesLabel.setVisible(true);
            }
        }
    }
    private void updatePanelForCsNob(){
        numOfBytesLabel.setText(String.valueOf(messagePart.getNumOfBytes()));
        messageTextField.setEnabled(false);
        numOfBytesComboBox.setVisible(false);
        numOfBytesLabel.setVisible(true);
    }
}
