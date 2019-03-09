package com.calvin.tms.service;

import com.calvin.tms.Database;
import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VehicleService {

    @Autowired
    private RoadService roadService;
    @Autowired
    private VehicleMomentService vehicleMomentService;
    @Autowired
    private Database database;

//    public static void main(String[] args) {
//
//        RoadService instance = RoadService.getInstance();
//        instance.generateRoad(10, 10);
//
//        Vehicle vehicle = new Vehicle();
//        vehicle.setCx(0);
//        vehicle.setCy(5);
//        vehicle.setDx(5);
//        vehicle.setDy(9);
//        driveCarToDestination(vehicle);
//        vehicle.setCx(0);
//        vehicle.setCy(5);
//        vehicle.setDx(6);
//        vehicle.setDy(0);
//        driveCarToDestination(vehicle);
//
//
//    }

    public Vehicle createVehicle(Vehicle vehicle) {
        String s = UUID.randomUUID().toString();
        vehicle.setId(s);
        database.getMap().put(s, vehicle);
        return vehicle;
    }

    private void driveCarToDestination(Vehicle vehicle) {
        roadService.raod.getCells()[vehicle.getCx()][vehicle.getCy()].setOccupied(true);
        Operation operation = initialMoment(vehicle);
        drive(vehicle, operation);
        operation = initialMoment(vehicle);
        drive(vehicle, operation);
        System.out.println(vehicle);
    }

    private Operation initialMoment(Vehicle vehicle) {

        int directionOfY = 0;
        int dy;
        if (vehicle.getDy() > vehicle.getCy()) {
            directionOfY = vehicle.getCy() + 1;
            dy = 1;
        } else {
            directionOfY = vehicle.getCy() - 1;
            dy = -1;
        }

        Cell cell = roadService.raod.getCells()[vehicle.getCx()][directionOfY];
        if (cell.isEnable()) {
            if (dy < 0 && cell.getOperation().contains(Operation.Y_MINUS)) {
                return Operation.Y_MINUS;
            } else if (dy > 0 && cell.getOperation().contains(Operation.Y_PLUS)) {
                return Operation.Y_PLUS;
            }
        }

        int directionOfX = 0;
        int dx;
        if (vehicle.getDx() > vehicle.getCx()) {
            directionOfX = vehicle.getCx() + 1;
            dx = 1;
        } else {
            directionOfX = vehicle.getCx() - 1;
            dx = -1;
        }

        cell = roadService.raod.getCells()[directionOfX][vehicle.getCy()];
        if (dx > 0 && cell.getOperation().contains(Operation.X_PLUS)) {
            return Operation.X_PLUS;
        } else if (dx < 0 && cell.getOperation().contains(Operation.X_MINUS)) {
            return Operation.X_MINUS;
        }
        return null;

    }

    private void drive(Vehicle vehicle, Operation initialOp) {

        if (initialOp == Operation.Y_MINUS || initialOp == Operation.Y_PLUS) {
            while (vehicle.getCy() != vehicle.getDy()) {
                driveTillDestination(vehicle, initialOp);
            }
        } else {
            while (vehicle.getCx() != vehicle.getDx()) {
                driveTillDestination(vehicle, initialOp);
            }
        }
    }

    private void driveTillDestination(Vehicle vehicle, Operation initialOp) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vehicleMomentService.moveVehicle(vehicle, initialOp);
        roadService.printRoad(10, 10);
    }


}
