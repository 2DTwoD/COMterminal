package com.goznak.visualization;

import com.goznak.communication.Connection;
import com.goznak.utils.Logger;
import com.goznak.utils.Saver;
import com.goznak.visualization.components.VerticalLayout;
import com.goznak.visualization.panels.ComParametersPanel;
import com.goznak.visualization.panels.TerminalPanel;
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
    PublishSubject<Boolean> panelUpdater;
    public MainWindow(Saver saver, Connection connection, ComParametersPanel comParametersPanel, TerminalPanel terminalPanel, PublishSubject<Boolean> panelUpdater) throws HeadlessException {
        this.panelUpdater = panelUpdater;
        this.connection = connection;
        this.comParametersPanel = comParametersPanel;
        this.terminalPanel = terminalPanel;
        this.saver = saver;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(mainPanel, VerticalLayout.CENTER));
        mainPanel.add(new JLabel("Терминал для последовательного порта"));
        mainPanel.add(comParametersPanel);
        mainPanel.add(terminalPanel);

        JScrollPane mainScroll = new JScrollPane(mainPanel);

        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1200,800));
        setTitle("COMterminal");
        add(mainScroll);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.info("Закрываю приложение");
                connection.closeConnection();
                comParametersPanel.setNewParametersFromComboBox();
                saver.save();
                Logger.info("Приложение закрыто");
                System.exit(0);
            }
        });

        panelUpdater.subscribe(val -> {
            updatePanel(comParametersPanel);
            updatePanel(terminalPanel);
            updatePanel(mainPanel);
            SwingUtilities.invokeLater(this::revalidate);
        });
        panelUpdater.onNext(true);

        terminalPanel.updateMessageLine();
        pack();
    }
    private void updatePanel(JComponent component){
        SwingUtilities.invokeLater(component::repaint);
        SwingUtilities.invokeLater(component::revalidate);
    }
}
