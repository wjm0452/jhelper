package com.jhelper.export.excel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SimpleCell {

    enum Type {
        BOOLEAN, NUMBER, INTEGER, DOUBLE, STRING, DATE, DATETIME;

        static public Type getTypeFor(Object value) {
            if (value instanceof Boolean) {
                return Type.BOOLEAN;
            } else if (value instanceof Integer) {
                return Type.INTEGER;
            } else if (value instanceof Double) {
                return Type.DOUBLE;
            } else if (value instanceof String) {
                return Type.STRING;
            } else if (value instanceof java.util.Date) {
                return Type.DATE;
            } else if (value instanceof LocalDate) {
                return Type.DATE;
            } else if (value instanceof LocalDateTime) {
                return Type.DATETIME;
            }

            return Type.STRING;
        }
    }

    private Type type;
    private Object value;

    public SimpleCell(Object value) {
        this.type = Type.getTypeFor(value);
        this.value = value;
    }

    public SimpleCell(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setValue(Object value) {
        this.value = value;

        if (type == null) {
            type = Type.getTypeFor(value);
        }
    }

    public Object getValue() {
        return value;
    }

}
