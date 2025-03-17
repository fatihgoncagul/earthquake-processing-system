import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import { Client } from '@stomp/stompjs';
import toast, { Toaster } from 'react-hot-toast';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const MapComponent = () => {
    const [earthquakes, setEarthquakes] = useState([]);
    const [historicalEarthquakes, setHistoricalEarthquakes] = useState([]);
    const [radarEffectMarkers, setRadarEffectMarkers] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8086/earthquake/getAll')
            .then(response => response.json())
            .then(data => {
                setHistoricalEarthquakes(data);
            })
            .catch(error => console.error("🚨 Error fetching earthquakes from cassandra-db:", error));

        const stompClient = new Client({
            brokerURL: 'ws://localhost:8086/ws/websocket',
            reconnectDelay: 5000,
        });

        stompClient.onConnect = (frame) => {
            console.log('✅ STOMP connected:', frame);

            stompClient.subscribe('/topic/earthquakes', (message) => {
                try {
                    const earthquake = JSON.parse(message.body);
                    setEarthquakes((prevEarthquakes) => [...prevEarthquakes, earthquake]);

                    if (earthquake.anomaly) {
                        setRadarEffectMarkers((prevMarkers) => [...prevMarkers, earthquake.latitude]);
                        setTimeout(() => {
                            setRadarEffectMarkers((prevMarkers) => prevMarkers.filter(latitude => latitude !== earthquake.latitude));
                        }, 10000);
                    }
                } catch (error) {
                    console.error("🚨 JSON parse error:", error);
                }
            });
        };

        stompClient.onStompError = (frame) => {
            console.error("STOMP error:", frame);
        };

        stompClient.activate();

        return () => {
            stompClient.deactivate();
        };
    }, []);

    const getColor = (magnitude) => {
        if (magnitude < 3) return 'turquoise';
        if (magnitude < 4) return 'green';
        if (magnitude < 5) return 'yellow';
        if (magnitude < 6) return 'orange';
        if (magnitude < 7) return 'red';
        return 'maroon';
    };

    const sendRequest = async (url, message) => {
        try {
            await fetch(url, { method: 'GET' });
            toast.success(`${message}`);
        } catch (error) {
            toast.error('Error!');
        }
    };

    return (
        <div style={{ position: 'relative' }}>
            <Toaster position="top-center" />

            <div style={{
                position: 'absolute', top: 20, right: 20, display: 'flex', gap: '10px', zIndex: 1000
            }}>
                <button
                    style={{
                        backgroundColor: 'green', color: 'white', padding: '12px 18px',
                        border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '16px',
                        transition: 'all 0.2s', boxShadow: '2px 2px 5px rgba(0,0,0,0.2)'
                    }}
                    onClick={() => sendRequest('http://localhost:8086/earthquake/generate', "Earthquake generation started!")}>
                    Generate Earthquakes
                </button>

                <button
                    style={{
                        backgroundColor: 'red', color: 'white', padding: '12px 18px',
                        border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '16px',
                        transition: 'all 0.2s', boxShadow: '2px 2px 5px rgba(0,0,0,0.2)'
                    }}
                    onClick={() => sendRequest('http://localhost:8086/earthquake/stopGenerating', "Earthquake generation stopped!")}>
                    Stop Generating
                </button>
            </div>

            <MapContainer center={[0, 0]} zoom={2} style={{ height: '100vh', width: '100%' }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                />

                {historicalEarthquakes.map((quake, index) => (
                    <Marker
                        key={`historical-${index}`}
                        position={[quake.latitude, quake.longitude]}
                        icon={L.divIcon({
                            className: 'custom-icon',
                            html: `<div class="quake-marker" style="
                                background-color: ${getColor(quake.magnitude)};
                                width: ${quake.magnitude * 4}px;
                                height: ${quake.magnitude * 4}px;
                                border-radius: 50%;
                                position: relative;
                            "></div>`
                        })}>
                        <Popup>
                            <strong>Magnitude:</strong> {quake.magnitude.toFixed(1)}<br/>
                            <strong>Coordinates:</strong> {quake.latitude.toFixed(4)}, {quake.longitude.toFixed(4)}
                        </Popup>
                    </Marker>
                ))}

                {earthquakes.map((quake, index) => (
                    <Marker
                        key={`live-${index}`}
                        position={[quake.latitude, quake.longitude]}
                        icon={L.divIcon({
                            className: 'custom-icon',
                            html: `<div class="quake-marker" style="
                                background-color: ${getColor(quake.magnitude)};
                                width: ${quake.magnitude * 4}px;
                                height: ${quake.magnitude * 4}px;
                                border-radius: 50%;
                                position: relative;
                                ${radarEffectMarkers.includes(quake.latitude) ? 'animation: radar-effect 3s infinite;' : ''}
                            "></div>`
                        })}>
                        <Popup>
                            <strong>Magnitude:</strong> {quake.magnitude.toFixed(1)}<br/>
                            <strong>Coordinates:</strong> {quake.latitude.toFixed(4)}, {quake.longitude.toFixed(4)}
                        </Popup>
                    </Marker>
                ))}
            </MapContainer>

            <style>
                {`
                    @keyframes radar-effect {
                        0% {
                            box-shadow: 0 0 0 0 rgba(255, 0, 0, 0.6);
                        }
                        100% {
                            box-shadow: 0 0 20px 20px rgba(255, 0, 0, 0);
                        }
                    }
                `}
            </style>
        </div>
    );
};

export default MapComponent;
