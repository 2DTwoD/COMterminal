package com.goznak.communication;

import jssc.SerialPort;
import jssc.SerialPortList;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor
public class ComParameters {
    private String name = "";
    private int baudRate = 9600;
    private int dataBits = 8;
    private CommonPars stopBits = stopBitsArray[0];
    private CommonPars parity = parityArray[0];
    private CommonPars flowControl = flowControlArray[0];
    private int mask = SerialPort.MASK_RXCHAR;
    public void setNewParameters(ComParameters newParameters){
        this.name = newParameters.getName();
        this.baudRate = newParameters.getBaudRate();
        this.dataBits = newParameters.getDataBits();
        this.stopBits = newParameters.getStopBits();
        this.parity = newParameters.getParity();
        this.flowControl = newParameters.getFlowControl();
    }

    static private String[] namesArray = SerialPortList.getPortNames();
    static public String[] getNamesArray(){
        if (namesArray.length == 0) {
            return new String[]{"Портов не найдено"};
        }
        return namesArray;
    }

    static public Integer[] baudRatesArray = {
            SerialPort.BAUDRATE_110,
            SerialPort.BAUDRATE_300,
            SerialPort.BAUDRATE_600,
            SerialPort.BAUDRATE_1200,
            SerialPort.BAUDRATE_2400,
            SerialPort.BAUDRATE_4800,
            SerialPort.BAUDRATE_9600,
            SerialPort.BAUDRATE_14400,
            SerialPort.BAUDRATE_19200,
            SerialPort.BAUDRATE_38400,
            SerialPort.BAUDRATE_57600,
            SerialPort.BAUDRATE_115200,
            SerialPort.BAUDRATE_128000,
            SerialPort.BAUDRATE_256000
    };
    static public Integer[] dataBitsArray = {
            SerialPort.DATABITS_5,
            SerialPort.DATABITS_6,
            SerialPort.DATABITS_7,
            SerialPort.DATABITS_8
    };
    static public CommonPars[] stopBitsArray = {
            new CommonPars("1", SerialPort.STOPBITS_1),
            new CommonPars("1.5", SerialPort.STOPBITS_1_5),
            new CommonPars("2", SerialPort.STOPBITS_2)
    };
    static public CommonPars[] parityArray = {
            new CommonPars("Нет", SerialPort.PARITY_NONE),
            new CommonPars("Нечетный", SerialPort.PARITY_ODD),
            new CommonPars("Четный", SerialPort.PARITY_EVEN),
            new CommonPars("Маркер", SerialPort.PARITY_MARK),
            new CommonPars("Пробел", SerialPort.PARITY_SPACE),
    };
    static public CommonPars[] flowControlArray = {
            new CommonPars("Нет", SerialPort.FLOWCONTROL_NONE),
            new CommonPars("Xon/Xoff", SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT),
            new CommonPars("Аппаратное", SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT)
    };
}
