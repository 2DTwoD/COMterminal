package com.goznak.visualization;

import com.goznak.communication.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {
    final
    Connection connection;
    final
    ComParametersPanel comParametersPanel;
    public MainWindow(Connection connection, ComParametersPanel comParametersPanel) throws HeadlessException {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(mainPanel, VerticalLayout.CENTER));
        this.connection = connection;
        this.comParametersPanel = comParametersPanel;
        setSize(1200,800);
        setLayout(new FlowLayout());
        mainPanel.add(new JLabel("Терминал для последовательного порта"));
        mainPanel.add(comParametersPanel);
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(this::pack);
    }
}
