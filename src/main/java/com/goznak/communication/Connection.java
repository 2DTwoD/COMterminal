package com.goznak.communication;

import com.goznak.utils.Logger;
import com.goznak.visualization.panels.TerminalPanel;
import io.reactivex.rxjava3.subjects.PublishSubject;
import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

@Component
public class Connection {
    final
    ComParameters comParameters;
    final
    PublishSubject<String> receivedData;
    SerialPort serialPort;

    public Connection(ComParameters comParameters, PublishSubject<String> receivedData) {
        this.comParameters = comParameters;
        this.receivedData = receivedData;
    }

    public void openConnection() {
        if(portOpened()){
            return;
        }
        Logger.info("Открываю соединение");
        serialPort = new SerialPort(comParameters.getName());
        try {
            serialPort.openPort();
            serialPort.setParams(comParameters.getBaudRate(),
                    comParameters.getDataBits(),
                    comParameters.getStopBits().value(),
                    comParameters.getParity().value());
            serialPort.setFlowControlMode(comParameters.getFlowControl().value());
            serialPort.addEventListener(event -> {
                try {
                    receivedData.onNext(serialPort.readString(event.getEventValue()));
                } catch (SerialPortException e) {
                    Logger.error("Read data from port error", e);
                }
            }, comParameters.getMask());
        }
        catch (SerialPortException e) {
            closeConnection();
            Logger.error("Open serial port error", e);
        }
        Logger.info("Соединение открыто");
    }
    public void writeData(byte[] data){
        if(portClosed()){
            return;
        }
        try {
            serialPort.writeBytes(data);
        } catch (SerialPortException e) {
            closeConnection();
            Logger.error("Write data to port error", e);
        }
    }
    public void closeConnection(){
        if(portClosed()) {
            return;
        }
        Logger.info("Закрываю соединение");
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            Logger.error("Close serial port error", e);
        }
        Logger.info("Соединение закрыто");
    }
    public boolean portOpened(){
        return serialPort != null && serialPort.isOpened();
    }
    public boolean portClosed(){
        return !portOpened();
    }
}
