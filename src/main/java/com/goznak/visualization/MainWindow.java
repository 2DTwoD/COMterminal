package com.goznak.visualization;

import com.goznak.communication.Connection;
import com.goznak.utils.Saver;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class MainWindow extends JFrame {
    final
    Saver saver;
    final
    Connection connection;
    final
    ComParametersPanel comParametersPanel;
    final
    TerminalPanel terminalPanel;
    public MainWindow(Saver saver, Connection connection, ComParametersPanel comParametersPanel, TerminalPanel terminalPanel) throws HeadlessException {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(mainPanel, VerticalLayout.CENTER));
        this.connection = connection;
        this.comParametersPanel = comParametersPanel;
        this.terminalPanel = terminalPanel;
        setPreferredSize(new Dimension(1200,800));
        setLayout(new FlowLayout());
        mainPanel.add(new JLabel("Терминал для последовательного порта"));
        mainPanel.add(comParametersPanel);
        mainPanel.add(terminalPanel);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.saver = saver;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                comParametersPanel.setNewParametersFromComboBox();
                saver.save();
                System.exit(0);
            }
        });
        SwingUtilities.invokeLater(this::revalidate);
        SwingUtilities.invokeLater(this::pack);
    }
}
