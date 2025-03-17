# 🌍 Earthquake DataStream Processing System

## 📌 Overview
This is a **Mono Repo** containing a **Real-time Earthquake Monitoring System**. The system generates random earthquake data,sends it to**Kafka**, processes it through **Flink**, and sends anomalies to **Kafka**. The backend serves as a Rest API for starting generation and database operations WebSocket publisher,   while the frontend visualizes the data using **React**.
Flink marks an earthquake as an anomaly if its magnitude is high and if more than two earthquakes occured close to each other within a specified perimeter in the last three minutes.

## 🚀 Tech Stack

- Java & Spring Boot
- Kafka
- Flink (Stream Processing)
- Websockets
- Cassandra (NoSQL Database)
- React

## ⚙️ Setup & Run Instructions

### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/fatihgoncagul/earthquake-processing-system.git
cd earthquake-monitoring-system
```

### **2️⃣ Start Services with Docker Compose**
```bash
docker-compose up -d --build
```
_This will start all of the containers, setup may take few minutes._

### **3️⃣ Open the Web UI**
- **Frontend UI:** `http://localhost:3000`
- **Flink Dashboard:** `http://localhost:8081` 
- **Backend API:** `http://localhost:8086`

---
Feel free to contribute and contact with me. Thank you.

