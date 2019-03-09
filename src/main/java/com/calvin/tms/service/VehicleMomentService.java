package com.calvin.tms.service;

import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Operation;

public class VehicleMomentService {


    private static VehicleMomentService ourInstance = new VehicleMomentService();

    public static VehicleMomentService getInstance() {
        return ourInstance;
    }

    private VehicleMomentService() {
    }

    public void moveVehicle(Vehicle vehicle, Operation operation) {

        RoadService instance = RoadService.getInstance();
        Vehicle move = instance.move(vehicle, operation);
    }
}
