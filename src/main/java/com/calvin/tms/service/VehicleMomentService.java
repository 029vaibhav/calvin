package com.calvin.tms.service;

import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleMomentService {

    @Autowired
    private RoadService roadService;

    public void moveVehicle(Vehicle vehicle, Operation operation) {
        roadService.move(vehicle, operation);
    }
}
