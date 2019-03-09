package com.calvin.tms.model.enums;

import lombok.Getter;

public enum Operation {

    X_PLUS(">"),
    X_MINUS("<"),
    Y_PLUS("^"),
    Y_MINUS("v");

    @Getter
    String operator;

    Operation(String v) {
        this.operator = v;
    }
}
