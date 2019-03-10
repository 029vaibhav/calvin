package com.calvin.tms.model;

import com.calvin.tms.model.enums.Operation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cell {

    int x;
    int y;
    List<Operation> operation = new ArrayList<>();
    boolean externalDecision;
    boolean enable;
    boolean occupied;
    String lat;
    String lon;
    boolean last;

    public Cell() {
    }

    public Cell(int x, int y, boolean externalDecision) {
        this.x = x;
        this.y = y;
        this.externalDecision = externalDecision;
    }
}
