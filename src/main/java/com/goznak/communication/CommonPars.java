package com.goznak.communication;

public record CommonPars(String name, int value) {
    @Override
    public String toString() {
        return name;
    }
}
