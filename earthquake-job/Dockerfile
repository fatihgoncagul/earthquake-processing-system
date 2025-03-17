# 🔹 1. Maven ile JAR dosyasını build eden bir "builder" aşaması
FROM maven:3.8.6-openjdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM flink:latest
COPY --from=builder /app/target/earthquake-job-1.0.jar /opt/flink/usrlib/job.jar

