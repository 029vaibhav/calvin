package com.calvin.tms.service;

import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Operation;

public class MainService {

    public static void main(String[] args) {

        RoadService instance = RoadService.getInstance();
        instance.generateRoad(10, 10);

        VehicleMomentService vehicleMomentService = VehicleMomentService.getInstance();

        Vehicle vehicle = new Vehicle();
        Operation operation = initiaMoment(vehicle);
        drive(vehicle, operation);
        operation = initiaMoment(vehicle);
        drive(vehicle, operation);
        System.out.println(vehicle);


    }

    private static Operation initiaMoment(Vehicle vehicle) {

        int cy = vehicle.getCy();
        int dy = vehicle.getDy() - cy;
        int directionOfY = 0;
        if (dy > 0) {
            directionOfY = vehicle.getCy() + 1;
        } else {
            directionOfY = vehicle.getCy() - 1;
        }

        Cell cell = RoadService.getInstance().raod.getCells()[cy][directionOfY];
        if (cell.isEnable()) {
            if (dy > 0) {
                return Operation.Y_PLUS;
            } else {
                return Operation.Y_MINUS;
            }
        }
        int dx = vehicle.getDx() - vehicle.getCx();
        if (dx > 0) {
            return Operation.X_PLUS;
        } else {
            return Operation.X_MINUS;
        }

    }

    private static void drive(Vehicle vehicle, Operation initialOp) {

        if (initialOp == Operation.Y_MINUS || initialOp == Operation.Y_PLUS) {
            while (vehicle.getCy() == vehicle.getDy()) {
                VehicleMomentService.getInstance().moveVehicle(vehicle, initialOp);
            }
        } else {
            while (vehicle.getCx() == vehicle.getDx()) {
                VehicleMomentService.getInstance().moveVehicle(vehicle, initialOp);
            }
        }
    }

}
