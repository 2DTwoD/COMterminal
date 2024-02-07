package com.goznak.visualization;

import com.goznak.types.MessagePart;
import com.goznak.types.MessageStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TerminalPanel extends JPanel {
    final
    MessageStructure messageStructure;
    final
    ApplicationContext context;
    JTextArea terminalTextArea = new JTextArea(10, 100);
    JPanel messageLine = new JPanel();
    public TerminalPanel(MessageStructure messageStructure, ApplicationContext context) {
        super();
        messageLine.setLayout(new VerticalLayout(this, VerticalLayout.LEFT));
        JButton addMessagePartButton = new JButton("Добавить часть");
        JLabel resultLabel = new JLabel("Результат");
        setLayout(new VerticalLayout(this, VerticalLayout.LEFT));
        addMessagePartButton.addActionListener(e -> {
            messageLine.add(context.getBean(MessagePartPanel.class));
        });
        add(messageLine);
        add(addMessagePartButton);
        add(resultLabel);
        add(terminalTextArea);
        SwingUtilities.invokeLater(this::revalidate);
        SwingUtilities.invokeLater(this::repaint);
        this.messageStructure = messageStructure;
        this.context = context;
    }

}
