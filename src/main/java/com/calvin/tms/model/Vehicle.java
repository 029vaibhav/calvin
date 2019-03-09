package com.calvin.tms.model;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class Vehicle {

    String id;
    long speed;
    double lat;
    double longitude;
    Date timeStamp;
    int currentDirection;
    int nextDirection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
