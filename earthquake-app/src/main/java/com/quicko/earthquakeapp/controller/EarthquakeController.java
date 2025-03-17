package com.quicko.earthquakeapp.controller;

import com.quicko.earthquakeapp.model.Earthquake;
import com.quicko.earthquakeapp.service.EarthquakeService;
import com.quicko.earthquakeapp.service.RandomDataGeneratorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("earthquake")
public class EarthquakeController {

    private final EarthquakeService earthquakeService;
    private final RandomDataGeneratorService randomDataGeneratorService;

    public EarthquakeController(EarthquakeService earthquakeService, RandomDataGeneratorService randomDataGeneratorService) {
        this.earthquakeService = earthquakeService;
        this.randomDataGeneratorService = randomDataGeneratorService;
    }


    @GetMapping("getAll")
    public List<Earthquake> getAllEarthquakes() {
        return earthquakeService.getAllEarthquakes();
    }

    @GetMapping("getAnormal")
    public List<Earthquake> getAnormalEarthquakes() {
        return earthquakeService.getAnormalEarthquakes();
    }

    @GetMapping("generate")
    public String generateEarthquakes() throws Exception {
        return randomDataGeneratorService.startGeneratingRandomData();
    }

    @GetMapping("stopGenerating")
    public String stopGenerating() {
        return randomDataGeneratorService.stopGeneratingRandomData();

    }


}
