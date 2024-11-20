package com.example.publisherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var mqttHelper: MqttHelper
    private lateinit var locationHelper: LocationHelper
    private lateinit var studentIdEditText: EditText
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mqttHelper = MqttHelper(this)
        locationHelper = LocationHelper(this)

        studentIdEditText = findViewById(R.id.studentIdEditText)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        status = findViewById(R.id.tvStatus)

        startButton.setOnClickListener {
            startPublishing()
        }

        stopButton.setOnClickListener {
            stopPublishing()
        }

        requestLocationPermissions()
    }

    private fun startPublishing() {
        val studentId = studentIdEditText.text.toString()
        if (studentId.isEmpty()) {
            Toast.makeText(this, "Please enter your student ID", Toast.LENGTH_SHORT).show()
            return
        }

        status.text = "Publishing Location"

        mqttHelper.connect(studentId)
        locationHelper.startLocationUpdates { location ->
            val locationData = JSONObject().apply {
                put("studentId", studentId)
                put("latitude", location.latitude)
                put("longitude", location.longitude)
                put("speed", location.speed)
                put("timestamp", System.currentTimeMillis())
            }
            mqttHelper.publish(locationData.toString())
            Log.d("PUBLISHED",locationData.toString())
        }
    }

    private fun stopPublishing() {
        status.text = "Not Publishing Location"
        locationHelper.stopLocationUpdates()
        mqttHelper.disconnect()
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show()
            }
        }
    }
}
