package com.goznak.types;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.nio.ByteBuffer;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessagePart {
    Func func;
    String value;
    int numOfBytes;

    public MessagePart() {
        this(Func.CHARS, "", 1);
    }

    public MessagePart(Func func, String value, int numOfBytes) {
        this.func = func;
        this.value = value;
        this.numOfBytes = numOfBytes;
    }
    public byte[] getHEX(){
        return switch(func){
            case DECIMAL -> ByteBuffer.allocate(numOfBytes).putInt(Integer.parseInt(value)).array();
            case FLOATING -> ByteBuffer.allocate(4).putFloat(Float.parseFloat(value)).array();
            case NUM_OF_BYTES -> new byte[]{1};
            case CHECK_SUM -> new byte[]{2};
            default -> value.getBytes();
        };
    }
}
