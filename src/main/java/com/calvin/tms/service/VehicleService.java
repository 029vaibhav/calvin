package com.calvin.tms.service;

import com.calvin.tms.Database;
import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Vehicle;
import com.calvin.tms.model.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

    public List<Vehicle> generateVehicles(int n) {

        List<Vehicle> vehicles = new ArrayList<>();
        List<Cell> activeCells = database.getActiveCells();
        List<Cell> lastCell = activeCells.stream().filter(Cell::isLast).collect(Collectors.toList());
        for (int i = 0; i < n; i++) {
            Vehicle vehicle = new Vehicle();
            Cell cell;
            do {
                int randomNumberInRange = getRandomNumberInRange(0, activeCells.size());
                cell = activeCells.get(randomNumberInRange);
            } while (cell.isOccupied());

            cell.setOccupied(true);
            vehicle.setCx(cell.getX());
            vehicle.setCy(cell.getY());
            vehicle.setLat(cell.getLat());
            vehicle.setLon(cell.getLon());
            int randomNumberInRange = getRandomNumberInRange(0, lastCell.size());
            Cell cell1 = lastCell.get(randomNumberInRange);
            vehicle.setDx(cell1.getX());
            vehicle.setDy(cell1.getY());
            vehicles.add(vehicle);
            database.getMap().put(vehicle.getId(), vehicle);
        }
        return vehicles;
    }

    public void driveAllCars() {

        Set<Map.Entry<String, Vehicle>> entries = database.getMap().entrySet();

        ExecutorService executorService = Executors.newCachedThreadPool();
        entries.forEach(stringVehicleEntry -> {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    driveCarToDestination(stringVehicleEntry.getKey());
                }
            });
        });
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    public Vehicle createVehicle(Vehicle vehicle) {

        Cell cell = roadService.getRoad().getCells()[vehicle.getCx()][vehicle.getCy()];
        if (!cell.isEnable()) {
            throw new RuntimeException("invalid position");
        }
        String s = UUID.randomUUID().toString();
        vehicle.setId(s);
        database.getMap().put(s, vehicle);
        return vehicle;
    }

    public Vehicle driveCarToDestination(String id) {
        Vehicle vehicle = database.getMap().get(id);
        roadService.getRoad().getCells()[vehicle.getCx()][vehicle.getCy()].setOccupied(true);
        Operation operation = initialMoment(vehicle);
        drive(vehicle, operation);
        operation = initialMoment(vehicle);
        drive(vehicle, operation);
        System.out.println(vehicle);
        roadService.deAllocate(vehicle);
        return vehicle;
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

        Cell cell = roadService.getRoad().getCells()[vehicle.getCx()][directionOfY];
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

        cell = roadService.getRoad().getCells()[directionOfX][vehicle.getCy()];
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
        roadService.printRoad();
    }


}
