FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=builder /app/target/EarthquakeApp.jar EarthquakeApp.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/EarthquakeApp.jar"]
