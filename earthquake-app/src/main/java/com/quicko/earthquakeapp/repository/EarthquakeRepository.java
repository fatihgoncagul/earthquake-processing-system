package com.quicko.earthquakeapp.repository;

import com.quicko.earthquakeapp.model.Earthquake;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;


public interface EarthquakeRepository extends CassandraRepository<Earthquake, UUID> {
    List<Earthquake> findByAnomalyTrue();

}
