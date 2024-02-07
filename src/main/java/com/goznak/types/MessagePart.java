package com.goznak.types;

import java.nio.ByteBuffer;

public record MessagePart(Func func, String value, int numOfBytes) {
    public MessagePart() {
        this(Func.CHARS, "", 1);
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
