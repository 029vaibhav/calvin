package com.calvin.tms.model.enums;

import lombok.Getter;

public enum Operation {

    X_PLUS(">"),
    X_MINUS("<"),
    Y_PLUS("v"),
    Y_MINUS("ʌ");

    @Getter
    String operator;

    Operation(String v) {
        this.operator = v;
    }
}
