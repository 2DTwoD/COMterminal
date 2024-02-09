package com.goznak.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessagePart {
    Func func;
    String value;
    int numOfBytes;

    public MessagePart() {
        this(Func.funcsArray[0], "", 1);
    }

    public MessagePart(Func func, String value, int numOfBytes) {
        this.func = func;
        this.value = value;
        this.numOfBytes = numOfBytes;
    }
    public byte[] HEX(){
        switch(func.value()){
            case DECIMAL -> {
                try{
                    return Arrays.copyOfRange(
                            ByteBuffer.allocate(8).putLong(Long.parseLong(value)).array(),
                            8 - numOfBytes, 8
                    );
                }
                catch (NumberFormatException ignored){
                }
                return new byte[]{0};
            }
            case FLOATING -> {
                try{
                    return ByteBuffer.allocate(4).putFloat(Float.parseFloat(value)).array();
                }
                catch (NumberFormatException ignored){
                }
                return new byte[]{0};
            }
            case NUMBER_OF_BYTES -> {
                return new byte[]{1};
            }
            case CHECK_SUM -> {
                return new byte[]{2};
            }
            default ->  {
                return value.getBytes();
            }
        }
    }
}
