package com.quicko.earthquakeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.quicko.earthquakeapp.repository")
public class EarthquakeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EarthquakeAppApplication.class, args);
    }

}
