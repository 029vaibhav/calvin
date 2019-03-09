package com.calvin.tms.model;

import com.calvin.tms.model.enums.Operation;
import lombok.Data;

import java.util.UUID;

@Data
public class Vehicle {

    String id = UUID.randomUUID().toString();
    int px;
    int py;
    int cx;
    int cy;
    int dx;
    int dy;

}
