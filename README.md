# 🌍 Real-time Earthquake DataStream Processing System

## 📌 Overview
The system generates random earthquake data and sends it to **Kafka**, processes it through **Flink**, and sends anomalies to **Kafka**. The backend serves as a Rest API for starting/stopping data generation and database operations, WebSocket publisher, while the frontend visualizes the data using **React**.
Flink marks an earthquake as an anomaly if its magnitude is high and if more than two earthquakes occured close to each other within a specified perimeter in the specified time interval.

![Flow Diagram](https://drive.google.com/uc?export=view&id=14mIbS2gLxSuKPbepr56UsOfL5PCt8Te4)
![User Interface](https://drive.google.com/uc?export=view&id=1CQ1_HpE4H3Y3f2_M1jSOc0bWIucjZAPN)
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
cd earthquake-processing-system
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

