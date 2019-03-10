package com.calvin.tms.model;

import lombok.Data;

@Data
public class DataResponse {

    private int verticalX;
    private VerticalCells[][] verticalCells;
    private VerticalCells[][] horizontalCells;
    private int horizontalY;
}
