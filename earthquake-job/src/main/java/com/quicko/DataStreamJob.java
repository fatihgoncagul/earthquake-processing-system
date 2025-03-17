package com.quicko;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicko.AnomalyDetection;
import com.quicko.Earthquake;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.connectors.cassandra.CassandraSink;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.api.common.serialization.SerializationSchema;

import java.time.Duration;


public class DataStreamJob {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("earthquakes")
                .setGroupId("earthquake-app")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("processed-earthquakes")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .build();

        DataStream<String> kafkaStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<Earthquake> earthquakeStream = kafkaStream.map(new MapFunction<String, Earthquake>() {
            @Override
            public Earthquake map(String json) throws Exception {

                return objectMapper.readValue(json, Earthquake.class);
            }
        });

        DataStream<Earthquake> processedEarthquakes = earthquakeStream
                .map(eq -> {
                    if (eq.getMagnitude() > 6) {
                        eq.setAnomaly(true); // Sadece büyük olanları işaretle
                    }
                    return eq;
                });

        DataStream<String> jsonAllAmdLargeQuakeStream = processedEarthquakes.map(eq -> {

            return objectMapper.writeValueAsString(eq); // Earthquake nesnesini JSON formatına çevir
        });

        jsonAllAmdLargeQuakeStream.sinkTo(sink);
        CassandraSink.addSink(processedEarthquakes)
                .setHost("cassandra", 9042)
                .build();

        DataStream<Earthquake> anomalyQuakesStream = earthquakeStream

                .windowAll(TumblingProcessingTimeWindows.of(Duration.ofSeconds(200)))
                .process(new AnomalyDetection())
                .filter(Earthquake::isAnomaly);

        DataStream<String> jsonAnomalQuakeStream = anomalyQuakesStream.map(eq -> {

            return objectMapper.writeValueAsString(eq);
        });

        jsonAnomalQuakeStream.sinkTo(sink);


        CassandraSink.addSink(earthquakeStream)
                .setHost("cassandra", 9042)
                .build();

        env.execute("Flink Kafka Earthquake Processing");
    }
}
