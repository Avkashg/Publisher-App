package com.example.publisherapp

import android.content.Context
import android.util.Log
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client


class MqttHelper(private val context: Context) {
    private var mqttClient: Mqtt5BlockingClient? = null

    companion object {
        const val HOST = "broker-816027622.sundaebytestt.com"
        const val TOPIC = "assignment/location"
    }

    fun connect(studentId: String) {
        mqttClient = Mqtt5Client.builder()
            .identifier(studentId)
            .serverHost(HOST)
            .serverPort(1883)
            .build()
            .toBlocking()
        try {
            mqttClient!!.connectWith()
                .cleanStart(true) // Start a clean session
                .keepAlive(30) // Set keep-alive interval to 30 seconds
                .sessionExpiryInterval(60) // Set session expiry interval to 60 seconds
                .send() // Execute the connection
            Log.d("MQTT","Connected")
        } catch (e: Exception) {
            Log.e("MQTT", "Error occurred trying to connect to broker")
        }
    }

    fun publish(message: String) {
        if(mqttClient != null){
            mqttClient!!.publishWith()?.topic(TOPIC)?.payload(message.toByteArray())?.send()
            Log.d("MQTT","Message sent to $TOPIC")
        }else{
            Log.e("MQTT", "MqttClient is not initialized or connected")
        }
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
            Log.d("MQTT","Disconnected Successfully")
        } catch (e: Exception) {
            Log.e("MQTT","Error Disconnecting")
        }
    }
}