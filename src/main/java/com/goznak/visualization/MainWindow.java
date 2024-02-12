package com.goznak.visualization;

import com.goznak.communication.Connection;
import com.goznak.utils.Saver;
import io.reactivex.rxjava3.subjects.PublishSubject;
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
    final
    PublishSubject<Boolean> updater;
    public MainWindow(Saver saver, Connection connection, ComParametersPanel comParametersPanel, TerminalPanel terminalPanel, PublishSubject<Boolean> updater) throws HeadlessException {
        this.updater = updater;
        this.connection = connection;
        this.comParametersPanel = comParametersPanel;
        this.terminalPanel = terminalPanel;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(mainPanel, VerticalLayout.CENTER));

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
        updater.subscribe(val -> {
            updatePanel(mainPanel);
            updatePanel(comParametersPanel);
            updatePanel(terminalPanel);
            SwingUtilities.invokeLater(this::revalidate);
        });
        terminalPanel.updateMessageLine();
        updater.onNext(true);
        pack();
    }
    private void updatePanel(JComponent component){
        SwingUtilities.invokeLater(component::repaint);
        SwingUtilities.invokeLater(component::revalidate);
    }
}
