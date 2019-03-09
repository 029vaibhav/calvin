package com.calvin.tms.service;

import com.calvin.tms.model.TrafficSignal;
import com.calvin.tms.model.Vehicle;

import java.util.List;

public class EvaluatorService {

    public double getDensity(TrafficSignal trafficSignal) {
        return trafficSignal.getVehicles().size() / trafficSignal.getLengthOfRoad();
    }

    public double getAverageSpeedOfRoad(TrafficSignal trafficSignal) {
        double sum = trafficSignal.getVehicles().stream().mapToDouble(Vehicle::getSpeed).sum();
        return sum / trafficSignal.getVehicles().size();
    }

    public TrafficSignal getTrafficSignalWithHighestGCD(List<TrafficSignal> signalList) {
        return gcd(signalList);
    }

    public TrafficSignal gcd(List<TrafficSignal> signalList) {
        int i = 1;
        TrafficSignal result = signalList.get(0);

        int size = signalList.size();
        while (i < size) {
            if (getDensity(signalList.get(i)) % getDensity(result) == 0) {
                i++;
            } else {
                double v = getDensity(signalList.get(i)) % getDensity(result);
                result = signalList.stream().filter(trafficSignal -> getDensity(trafficSignal) == v).findFirst().get();
                i++;
            }
        }
        return result;
    }
}



