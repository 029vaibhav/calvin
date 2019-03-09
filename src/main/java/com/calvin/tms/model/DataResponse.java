package com.calvin.tms.model;

import lombok.Data;

import java.util.List;

@Data
public class DataResponse {

    List<List<List<String>>> data;
}
