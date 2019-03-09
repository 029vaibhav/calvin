package com.calvin.tms.service;

import com.calvin.tms.model.Route;
import lombok.Data;

import java.util.List;

@Data
public class LatLongRequest {
    private List<Route> routes;

}
