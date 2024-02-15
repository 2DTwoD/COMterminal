package com.goznak.message;

import com.goznak.types.FuncEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
@Getter
@Setter
public class MessageStructure implements Iterable<MessagePart>{
    final
    ApplicationContext context;
    List<MessagePart> partsList = new ArrayList<>();

    public MessageStructure(ApplicationContext context) {
        this.context = context;
    }

    public void setNewPartsList(List<MessagePart> newPartsList){
        for(MessagePart messagePartFromFile: newPartsList){
            MessagePart messagePartForAdding = context.getBean(MessagePart.class);
            messagePartForAdding.setFunc(messagePartFromFile.getFunc());
            messagePartForAdding.setValue(messagePartFromFile.getValue());
            messagePartForAdding.setNumOfBytes(messagePartFromFile.getNumOfBytes());
            partsList.add(messagePartForAdding);
        }
    }
    public void addPart(){
        partsList.add(context.getBean(MessagePart.class));
    }
    public void addPartBefore(MessagePart currentPart){
        partsList.add(partsList.indexOf(currentPart), context.getBean(MessagePart.class));
    }
    public void removePart(MessagePart part){
        partsList.remove(part);
    }
    public String getFullMessageHEX(){
        StringBuilder result = new StringBuilder();
        for(byte item: getFullMessageBytes()){
            result.append(String.format("%02x ", item));
        }
        return result.toString().toUpperCase();
    }
    public String getFullMessage(){
        StringBuilder result = new StringBuilder();
        for(MessagePart part: partsList){
            result.append(String.format("%s ", part.getValue()));
        }
        return result.toString();
    }
    public byte[] getFullMessageBytes(){
        Stream<Byte> resultStream = Stream.empty();
        for(MessagePart part: partsList){
            Byte[] curArray = ArrayUtils.toObject(part.HEX());
            resultStream = Stream.concat(resultStream, Stream.of(curArray));
        }
        return ArrayUtils.toPrimitive(resultStream.toArray(Byte[]::new));
    }
    public byte getNumOfBytes(){
        return partsList.stream().reduce(0, (x, y) -> x + y.getNumOfBytes(), Integer::sum).byteValue();
    }
    public byte getCheckSumXor(){
        return partsList.stream()
                .filter(i -> !(i.getFunc().value().equals(FuncEnum.CHECK_SUM_XOR)
                        || i.getFunc().value().equals(FuncEnum.CHECK_SUM_CRC16_CCITT)))
                .reduce((byte) 0, (x, y) -> (byte) (x ^ y.checkSumXor()), (x, y) -> (byte) (x ^ y));
    }
    public int getCheckSumCrcCcit(){
        Byte[] array = partsList.stream()
                .filter(i -> !(i.getFunc().value().equals(FuncEnum.CHECK_SUM_XOR)
                        || i.getFunc().value().equals(FuncEnum.CHECK_SUM_CRC16_CCITT)))
                .map(MessagePart::HEX)
                .flatMap(i -> Arrays.stream(ArrayUtils.toObject(i))).toArray(Byte[]::new);
        return crc16(array);
    }
    int crc16(Byte[] messageBytes)
    {
        int crc = 0xFFFF;
        int polynomial = 0x1021;
        for (byte b:messageBytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xFFFF;
        return crc;
    }
    @Override
    public Iterator<MessagePart> iterator() {
        return partsList.iterator();
    }

}
