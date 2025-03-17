package com.quicko;

import com.quicko.Earthquake;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.locationtech.spatial4j.distance.DistanceUtils;

import java.util.ArrayList;
import java.util.List;

public class AnomalyDetection extends ProcessAllWindowFunction<Earthquake, Earthquake, TimeWindow> {


    @Override
    public void process(Context context, Iterable<Earthquake> earthquakes, Collector<Earthquake> out) {
        List<Earthquake> earthquakeList = new ArrayList<>();

        earthquakes.forEach(earthquakeList::add);

        for (Earthquake eq1 : earthquakeList) {
            int countNearby = 0;

            for (Earthquake eq2 : earthquakeList) {
                if (eq1.equals(eq2)) continue;

                double distance = calculateDistance(eq1.getLatitude(), eq1.getLongitude(),
                        eq2.getLatitude(), eq2.getLongitude());

                if (distance <= 500) {
                    countNearby++;
                }
            }
            if (countNearby >= 3) {
                System.out.println("changed" + eq1 + "as anomaly");
                eq1.setAnomaly(true);
                out.collect(eq1);
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double latDiff = Math.toRadians(lat2 - lat1);
        double lonDiff = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // KM cinsinden mesafe
    }
}
