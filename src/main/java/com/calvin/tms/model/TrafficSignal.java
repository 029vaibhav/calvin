package com.calvin.tms.model;

import lombok.Data;

import java.util.Set;

@Data
public class TrafficSignal {

    String id;
    Set<Vehicle> vehicles;
    int lengthOfRoad;
    double lat;
    double longitude;
}
