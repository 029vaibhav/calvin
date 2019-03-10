package com.calvin.tms;

import com.calvin.tms.model.Cell;
import com.calvin.tms.model.Raod;
import com.calvin.tms.model.Vehicle;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class Database {

    Map<String, Vehicle> map = new HashMap<>();
    List<Cell> activeCells = new ArrayList<>();
    Raod raod = new Raod();
    int max;
}
