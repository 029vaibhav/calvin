package com.calvin.tms;

import com.calvin.tms.model.Vehicle;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class Database {

    Map<String, Vehicle> map = new HashMap<>();
}
