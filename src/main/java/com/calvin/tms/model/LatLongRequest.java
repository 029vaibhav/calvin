package com.calvin.tms.model;

import lombok.Data;

import java.util.List;

@Data
public class LatLongRequest {
    private List<Horizontal> horizontal;
    private List<Horizontal> vertical;

}
