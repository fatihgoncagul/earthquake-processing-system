package com.quicko;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;


@Table(keyspace = "earthquake_keyspace", name = "earthquake")
public class Earthquake {

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "magnitude")
    private Double magnitude;

    @Column(name = "anomaly")
    private boolean anomaly;

    public Earthquake() {
    }

    public Earthquake(Double latitude, Double longitude, Double magnitude, boolean anomaly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.magnitude = magnitude;
        this.anomaly = anomaly;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }


    public boolean isAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", magnitude=" + magnitude +
                ", anomaly=" + anomaly +
                '}';
    }
}
