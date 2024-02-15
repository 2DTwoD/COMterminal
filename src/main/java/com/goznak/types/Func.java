package com.goznak.types;

import java.util.Objects;

public record Func(String name, FuncEnum value) {
    public static Func[] funcsArray = new Func[]{
        new Func("Символы ASCII", FuncEnum.CHARS),
        new Func("Целое число", FuncEnum.DECIMAL),
        new Func("Дробное число", FuncEnum.FLOATING),
        new Func("Количество байт в сообщении", FuncEnum.NUMBER_OF_BYTES),
        new Func("Контрольная сумма XOR", FuncEnum.CHECK_SUM_XOR),
        new Func("Контрольная сумма CRC16-CCITT", FuncEnum.CHECK_SUM_CRC16_CCITT)
    };

    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Func func = (Func) o;
        return value == func.value && Objects.equals(name, func.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
