package com.quicko.earthquakeapp.model;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;




@Table("earthquake")  // Cassandra'daki tablo adı
public class Earthquake  {

    @PrimaryKey
    @Column("latitude")
    private double latitude;

    @Column("longitude")
    private double longitude;

    @Column("magnitude")
    private double magnitude;

    @Column("anomaly")
    private boolean anomaly;

    public boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }

    public Earthquake() {

    }

    public Earthquake( double latitude, double longitude, double magnitude) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.magnitude = magnitude;

    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", magnitude=" + magnitude +
                ", anomaly=" + anomaly +
                '}';
    }
}