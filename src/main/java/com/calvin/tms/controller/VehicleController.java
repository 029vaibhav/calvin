package com.calvin.tms.controller;

import com.calvin.tms.model.Vehicle;
import com.calvin.tms.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping(value = "/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicles(@RequestBody Vehicle vehicle) {

        Vehicle vehicle1 = vehicleService.createVehicle(vehicle);
        return new ResponseEntity<>(vehicle1, HttpStatus.CREATED);

    }

    @GetMapping(value = "/v1/vehicles/{n}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicles(@PathVariable int n) {

        List<Vehicle> vehicles = vehicleService.generateVehicles(n);
        return new ResponseEntity<>(vehicles, HttpStatus.CREATED);

    }

    @GetMapping(value = "/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicles() {

        vehicleService.driveAllCars();
        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @PostMapping(value = "/v1/vehicles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicles(@PathVariable String id) {

        Vehicle vehicle1 = vehicleService.driveCarToDestination(id);
        return new ResponseEntity<>(vehicle1, HttpStatus.OK);

    }

}
