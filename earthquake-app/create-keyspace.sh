#!/bin/bash

echo "Cassandra'nın başlamasını bekliyoruz..."
until cqlsh cassandra -e "DESCRIBE KEYSPACES"; do
  echo "Cassandra henüz hazır değil, bekleniyor..."
  sleep 5
done

echo "Keyspace oluşturuluyor..."
cqlsh cassandra <<EOF
CREATE KEYSPACE IF NOT EXISTS earthquake_keyspace
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS earthquake_keyspace.earthquake (
    longitude DOUBLE,
    latitude DOUBLE,
    magnitude DOUBLE,
    anomaly BOOLEAN,
    PRIMARY KEY (latitude)
);
EOF

echo "Keyspace ve tablo başarıyla oluşturuldu!"