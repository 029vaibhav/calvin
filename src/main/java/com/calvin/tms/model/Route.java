package com.calvin.tms.model;

import lombok.Data;

import javax.print.attribute.standard.Destination;


@Data
public class Route {

    private Location destination;
    private Location source;
}
