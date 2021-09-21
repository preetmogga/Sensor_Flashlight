package com.preet.myapplication

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var cameraManager: CameraManager? = null
    private var textView: TextView? = null
    private var changeValue = 0f
    private var lightSensor: Sensor? = null
    private var cameraId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        } else {
            Toast.makeText(this, "Your phone no light Sensor", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onSensorChanged(event: SensorEvent) {
        changeValue = event.values[0]
        textView!!.text = changeValue.toString()
        if (changeValue < 50) {
            try {
                cameraManager!!.setTorchMode(cameraId!!, true)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        } else {
            try {
                cameraManager!!.setTorchMode(cameraId!!, false)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}