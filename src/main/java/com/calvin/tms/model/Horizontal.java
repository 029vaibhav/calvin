package com.calvin.tms.model;


import lombok.Data;

import javax.print.attribute.standard.Destination;

@Data
public class Horizontal {

    private Location destination;
    private Location source;
    private String direction;
}
