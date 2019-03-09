package com.calvin.tms.controller;

import com.calvin.tms.model.Raod;
import com.calvin.tms.service.LatLongRequest;
import com.calvin.tms.service.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoadController {

    @Autowired
    private RoadService roadService;

    @PostMapping(value = "/v1/roads", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createRoad(@RequestBody LatLongRequest latLongRequest) {

        Raod raod = roadService.generateRoad(latLongRequest);
        return new ResponseEntity<>(raod, HttpStatus.CREATED);

    }

}
