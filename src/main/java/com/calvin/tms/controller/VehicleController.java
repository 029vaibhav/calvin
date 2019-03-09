package com.calvin.tms.controller;

import com.calvin.tms.model.Vehicle;
import com.calvin.tms.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping(value = "/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createVehicles(@RequestBody Vehicle vehicle) {

        Vehicle vehicle1 = vehicleService.createVehicle(vehicle);
        return new ResponseEntity<>(vehicle1, HttpStatus.CREATED);

    }

}
