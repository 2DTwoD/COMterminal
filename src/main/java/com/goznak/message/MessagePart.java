package com.goznak.message;

import com.goznak.types.Func;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
@Getter
@Setter
@NoArgsConstructor
@Component
@Scope("prototype")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessagePart{
    Func func;
    String value;
    int numOfBytes;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    transient MessageStructure messageStructure;

    @Autowired
    public MessagePart(MessageStructure messageStructure) {
        this.messageStructure = messageStructure;
        this.func = Func.funcsArray[0];
        this.value = "";
        this.numOfBytes = 0;
    }

    public byte[] HEX(){
        byte[] result = new byte[]{};
        switch(func.value()){
            case DECIMAL -> {
                try{
                    result = Arrays.copyOfRange(
                            ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(Long.parseLong(value)).array(),
                            0, numOfBytes
                    );
                }
                catch (NumberFormatException ignored){
                    result = new byte[numOfBytes];
                }
            }
            case FLOATING -> {
                try{
                    result =  ByteBuffer.allocate(4).putFloat(Float.parseFloat(value)).array();
                }
                catch (NumberFormatException ignored){
                }
                numOfBytes = 4;
            }
            case NUMBER_OF_BYTES -> {
                result = new byte[]{messageStructure.getNumOfBytes()};
                numOfBytes = 1;
            }
            case CHECK_SUM_XOR -> {
                result = new byte[]{messageStructure.getCheckSumXor()};
                numOfBytes = 1;
            }
            case CHECK_SUM_CRC16_CCITT -> {
                result = Arrays.copyOfRange(
                        ByteBuffer.allocate(4).putInt(messageStructure.getCheckSumCrcCcit()).array(),
                        2, 4
                );
                numOfBytes = 2;
            }
            default -> {
                result = value.getBytes(StandardCharsets.US_ASCII);
                numOfBytes = result.length;
            }
        }
        return result;
    }
    public byte checkSumXor(){
        return Arrays.stream(ArrayUtils.toObject(HEX())).reduce((byte) 0, (x, y) -> (byte) (x ^ y));
    }
}
