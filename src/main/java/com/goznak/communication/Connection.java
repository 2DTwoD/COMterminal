package com.goznak.communication;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.springframework.stereotype.Component;

@Component
public class Connection {
    final
    ComParameters comParameters;
    SerialPort serialPort;

    public Connection(ComParameters comParameters) {
        this.comParameters = comParameters;
    }

    public void openConnection() {
        if(portOpened()){
            return;
        }
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
                    String data = serialPort.readString(event.getEventValue());
                } catch (SerialPortException e) {
                    throw new RuntimeException(e);
                }
            }, comParameters.getMask());
        }
        catch (SerialPortException e) {
            closeConnection();
            throw new RuntimeException(e);
        }
    }
    public void writeData(byte[] data){
        if(portClosed()){
            return;
        }
        try {
            serialPort.writeBytes(data);
        } catch (SerialPortException e) {
            closeConnection();
            throw new RuntimeException(e);
        }
    }
    public void closeConnection(){
        if(portClosed()) {
            return;
        }
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean portOpened(){
        return serialPort != null && serialPort.isOpened();
    }
    public boolean portClosed(){
        return !portOpened();
    }
}
