package kr.co.myproject.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BoardType{
    FREE,
    NOTICE;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static BoardType fromValue(String value) {
        return BoardType.valueOf(value.toUpperCase());
    }
}