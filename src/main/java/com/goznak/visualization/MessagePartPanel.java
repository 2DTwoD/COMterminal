package com.goznak.visualization;

import com.goznak.types.Func;
import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Scope("prototype")
public class MessagePartPanel extends JPanel {
    final
    MessageStructure messageStructure;
    JComboBox<Func> funcComboBox = new JComboBox<>(Func.values());
    LabelWithTextField numOfBytesComponent = new LabelWithTextField("Кол-во байт:", 2);
    LabelWithTextField messageComponent = new LabelWithTextField("Знач.:", 10);
    JButton deleteButton = new JButton("Убрать");
    @Autowired
    public MessagePartPanel(MessageStructure messageStructure) {
        super();
        this.messageStructure = messageStructure;
        MessagePart messagePart = new MessagePart();
        messageStructure.addPart(messagePart);
        deleteButton.addActionListener(e -> {
            this.getParent().remove(this);
            messageStructure.removePart(messagePart);
            SwingUtilities.invokeLater(getParent()::revalidate);
            SwingUtilities.invokeLater(getParent()::repaint);
        });
        add(funcComboBox);
        add(numOfBytesComponent);
        add(messageComponent);
        add(deleteButton);
        SwingUtilities.invokeLater(this::revalidate);
        SwingUtilities.invokeLater(this::repaint);
    }
}
