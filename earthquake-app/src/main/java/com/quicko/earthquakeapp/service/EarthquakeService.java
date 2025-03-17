package com.quicko.earthquakeapp.service;

import com.quicko.earthquakeapp.model.Earthquake;
import com.quicko.earthquakeapp.repository.EarthquakeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EarthquakeService {

    private final EarthquakeRepository earthquakeReposityory;

    public EarthquakeService(EarthquakeRepository earthquakeReposityory) {
        this.earthquakeReposityory = earthquakeReposityory;
    }

    public List<Earthquake> getAllEarthquakes() {
        return earthquakeReposityory.findAll();
    }

    public List<Earthquake> getAnormalEarthquakes() {
        return earthquakeReposityory.findByAnomalyTrue();
    }

}
