package com.goznak.visualization;

import com.goznak.communication.ComParameters;
import com.goznak.communication.CommonPars;
import com.goznak.communication.Connection;
import jssc.SerialPortList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ComParametersPanel extends JPanel {
    final
    ComParameters comParameters;
    final
    Connection connection;
    JComboBox<String> portNamesComboBox;
    JComboBox<Integer> baudRateComboBox;
    JComboBox<Integer> dataBitsComboBox;
    JComboBox<CommonPars> stopBitsComboBox;
    JComboBox<CommonPars> parityComboBox;
    JComboBox<CommonPars> flowControlComboBox;
    public ComParametersPanel(ComParameters comParameters, Connection connection) {
        super();
        setLayout(new VerticalLayout(this, VerticalLayout.CENTER));
        JPanel parametersPanel = new JPanel(new FlowLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        portNamesComboBox = getEditableComboBox(ComParameters.getNamesArray());
        baudRateComboBox = getEditableComboBox(ComParameters.baudRatesArray);
        dataBitsComboBox = new JComboBox<>(ComParameters.dataBitsArray);
        stopBitsComboBox = new JComboBox<>(ComParameters.stopBitsArray);
        parityComboBox = new JComboBox<>(ComParameters.parityArray);
        flowControlComboBox = new JComboBox<>(ComParameters.flowControlArray);

        portNamesComboBox.setSelectedItem(comParameters.getName());
        baudRateComboBox.setSelectedItem(comParameters.getBaudRate());
        dataBitsComboBox.setSelectedItem(comParameters.getDataBits());
        stopBitsComboBox.setSelectedItem(comParameters.getStopBits());

        JButton connectButton = new JButton("Подключиться");
        JButton disconnectButton = new JButton("Отключиться");
        addItem(parametersPanel, "Название порта:", portNamesComboBox);
        addItem(parametersPanel, "Бит в секунду:", baudRateComboBox);
        addItem(parametersPanel, "Стоповые биты:", stopBitsComboBox);
        addItem(parametersPanel, "Биты данных:", dataBitsComboBox);
        addItem(parametersPanel, "Четность:", parityComboBox);
        addItem(parametersPanel, "Управление потоком:", flowControlComboBox);
        buttonsPanel.add(connectButton);
        buttonsPanel.add(disconnectButton);
        add(parametersPanel);
        add(buttonsPanel);
        connectButton.addActionListener(e -> {
            comParameters.setName(getComboBoxValue(portNamesComboBox));
            comParameters.setBaudRate(getComboBoxValue(baudRateComboBox));
            comParameters.setStopBits(getComboBoxValue(stopBitsComboBox));
            comParameters.setDataBits(getComboBoxValue(dataBitsComboBox));
            comParameters.setParity(getComboBoxValue(parityComboBox));
            comParameters.setFlowControl(getComboBoxValue(flowControlComboBox));
            connection.openConnection();
        });
        this.comParameters = comParameters;
        this.connection = connection;
    }
    private<T> T getComboBoxValue(JComboBox<T> comboBox){
        return comboBox.getItemAt(comboBox.getSelectedIndex());
    }
    private void addItem(JPanel panel, String labelString, JComponent component){
        panel.add(new JLabel(labelString));
        panel.add(component);
    }
    private <T> JComboBox<T> getEditableComboBox(T[] array){
        JComboBox<T> comboBox = new JComboBox<>(array);
        comboBox.setEditable(true);
        return comboBox;
    }
}
