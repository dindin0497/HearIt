package com.accessibility.hearit

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    private val RECORD_AUDIO_REQUEST_CODE = 1

    lateinit var converter: SpeechToText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        converter  = RealSpeechToText(this.application)

        val tv = findViewById<TextView>(R.id.tv)

        converter.text.asLiveData().observe(this) { txt ->
           tv.text = txt
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE
            )
        } else {
            startListening()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult")
        if (requestCode == RECORD_AUDIO_REQUEST_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening()
        } else {
            Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListening() {
        Log.d(TAG, "startListening")
        converter.start()
    }

    override fun onDestroy() {
        converter.stop()
        super.onDestroy()
    }
}