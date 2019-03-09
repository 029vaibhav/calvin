package com.calvin.tms.model;

import com.calvin.tms.model.enums.Operation;
import com.calvin.tms.service.RoadService;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cell {

    int x;
    int y;
    List<Operation> operation = new ArrayList<>();
    boolean decision;
    boolean enable;
    boolean occupied;
    String lat;
    String lon;

    public Cell() {
    }

    public Cell(int x, int y, boolean decision) {
        this.x = x;
        this.y = y;
        this.decision = decision;
    }
}
