package com.goznak.visualization.panels;

import com.goznak.communication.ComParameters;
import com.goznak.types.CommonPars;
import com.goznak.communication.Connection;
import com.goznak.utils.Saver;
import com.goznak.visualization.components.VerticalLayout;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ComParametersPanel extends JPanel {
    final
    ComParameters comParameters;
    final
    Connection connection;
    final
    Saver saver;
    final
    PublishSubject<Boolean> panelUpdater;
    JComboBox<String> portNamesComboBox;
    JComboBox<Integer> baudRateComboBox;
    JComboBox<Integer> dataBitsComboBox;
    JComboBox<CommonPars> stopBitsComboBox;
    JComboBox<CommonPars> parityComboBox;
    JComboBox<CommonPars> flowControlComboBox;
    JButton connectButton = new JButton("Подключиться");
    JButton disconnectButton = new JButton("Отключиться");
    public ComParametersPanel(ComParameters comParameters, Connection connection, Saver saver, PublishSubject<Boolean> panelUpdater) {
        super();
        this.panelUpdater = panelUpdater;
        setLayout(new VerticalLayout(this, VerticalLayout.CENTER));
        JPanel parametersPanel = new JPanel(new FlowLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JLabel connectLabel = new JLabel("-", SwingConstants.CENTER);
        connectLabel.setOpaque(true);
        connectLabel.setPreferredSize(new Dimension(20, 20));
        portNamesComboBox = new JComboBox<>(ComParameters.getNamesArray());
        baudRateComboBox = new JComboBox<>(ComParameters.baudRatesArray);
        dataBitsComboBox = new JComboBox<>(ComParameters.dataBitsArray);
        stopBitsComboBox = new JComboBox<>(ComParameters.stopBitsArray);
        parityComboBox = new JComboBox<>(ComParameters.parityArray);
        flowControlComboBox = new JComboBox<>(ComParameters.flowControlArray);

        setSelectedItem(portNamesComboBox, ComParameters.getNamesArray(), comParameters.getName());
        setSelectedItem(baudRateComboBox, ComParameters.baudRatesArray, comParameters.getBaudRate());
        setSelectedItem(dataBitsComboBox, ComParameters.dataBitsArray, comParameters.getDataBits());
        setSelectedItem(stopBitsComboBox, ComParameters.stopBitsArray, comParameters.getStopBits());
        setSelectedItem(parityComboBox, ComParameters.parityArray, comParameters.getParity());
        setSelectedItem(flowControlComboBox, ComParameters.flowControlArray, comParameters.getFlowControl());

        addItem(parametersPanel, "Название порта:", portNamesComboBox);
        addItem(parametersPanel, "Бит в секунду:", baudRateComboBox);
        addItem(parametersPanel, "Стоповые биты:", stopBitsComboBox);
        addItem(parametersPanel, "Биты данных:", dataBitsComboBox);
        addItem(parametersPanel, "Четность:", parityComboBox);
        addItem(parametersPanel, "Управление потоком:", flowControlComboBox);
        buttonsPanel.add(connectButton);
        buttonsPanel.add(connectLabel);
        buttonsPanel.add(disconnectButton);
        add(parametersPanel);
        add(buttonsPanel);
        connectButton.addActionListener(e -> {
            setNewParametersFromComboBox();
            connection.openConnection();
        });
        disconnectButton.addActionListener(e ->{
            connection.closeConnection();
        });
        this.comParameters = comParameters;
        this.connection = connection;
        this.saver = saver;
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(() -> {
            if(connection.portOpened()){
                connectLabel.setText("+");
                connectLabel.setBackground(Color.GREEN);
                setEnabledForComponents(false);
            } else {
                connectLabel.setText("-");
                connectLabel.setBackground(Color.LIGHT_GRAY);
                setEnabledForComponents(true);
            }
        }, 0, 1, TimeUnit.SECONDS);
        panelUpdater.onNext(true);
    }
    public void setNewParametersFromComboBox(){
        comParameters.setName(getComboBoxValue(portNamesComboBox));
        comParameters.setBaudRate(getComboBoxValue(baudRateComboBox));
        comParameters.setStopBits(getComboBoxValue(stopBitsComboBox));
        comParameters.setDataBits(getComboBoxValue(dataBitsComboBox));
        comParameters.setParity(getComboBoxValue(parityComboBox));
        comParameters.setFlowControl(getComboBoxValue(flowControlComboBox));
    }
    private void setEnabledForComponents(boolean value){
        portNamesComboBox.setEnabled(value);
        baudRateComboBox.setEnabled(value);
        dataBitsComboBox.setEnabled(value);
        stopBitsComboBox.setEnabled(value);
        parityComboBox.setEnabled(value);
        flowControlComboBox.setEnabled(value);
        connectButton.setEnabled(value);
        disconnectButton.setEnabled(!value);
    }
    private<T> void setSelectedItem(JComboBox<T> comboBox, T[] array, T value){
        if(Arrays.asList(array).contains(value)) {
            comboBox.setSelectedItem(value);
        }
    }
    private<T> T getComboBoxValue(JComboBox<T> comboBox){
        return comboBox.getItemAt(Math.max(comboBox.getSelectedIndex(), 0));
    }
    private void addItem(JPanel panel, String labelString, JComponent component){
        panel.add(new JLabel(labelString));
        panel.add(component);
    }
}
