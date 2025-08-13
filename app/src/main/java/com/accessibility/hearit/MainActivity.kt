package com.accessibility.hearit

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.k2fsa.sherpa.ncnn.RecognizerConfig
import com.k2fsa.sherpa.ncnn.SherpaNcnn
import com.k2fsa.sherpa.ncnn.getDecoderConfig
import com.k2fsa.sherpa.ncnn.getFeatureExtractorConfig
import com.k2fsa.sherpa.ncnn.getModelConfig
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val useGPU: Boolean = true

    private lateinit var model: SherpaNcnn
    private var audioRecord: AudioRecord? = null
    private lateinit var recordButton: Button
    private lateinit var textView: TextView
    private var recordingThread: Thread? = null

    private val audioSource = MediaRecorder.AudioSource.MIC
    private val sampleRateInHz = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO

    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private var idx: Int = 0


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private var counter = 1

    @Volatile
    private var isRecording: Boolean = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }

        if (!permissionToRecordAccepted) {
            Log.e(TAG, "Audio record is disallowed")
            finish()
        }

        Log.i(TAG, "Audio record is permitted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        initModel()

        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter(mutableListOf())

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        recordButton = findViewById(R.id.record_button)
        recordButton.setOnClickListener { onclick() }

    }

    private fun onclick() {
        if (!isRecording) {
            val ret = initMicrophone()
            if (!ret) {
                Log.e(TAG, "Failed to initialize microphone")
                return
            }
            Log.i(TAG, "state: ${audioRecord?.state}")
            audioRecord!!.startRecording()
            recordButton.setText(R.string.stop)
            isRecording = true
//            textView.text = ""
            idx = 0

            adapter.clear()
            adapter.addItem("")

            recordingThread = thread(true) {
                model.reset(true)
                processSamples()
            }
            Log.i(TAG, "Started recording")
        } else {
            isRecording = false
            audioRecord!!.stop()
            audioRecord!!.release()
            audioRecord = null
            recordButton.setText(R.string.start)
            Log.i(TAG, "Stopped recording")
        }
    }

    private fun processSamples() {
        Log.i(TAG, "processing samples")

        val interval = 0.1 // i.e., 100 ms
        val bufferSize = (interval * sampleRateInHz).toInt() // in samples
        val buffer = ShortArray(bufferSize)
        var lastText: String = ""


        while (isRecording) {
            val ret = audioRecord?.read(buffer, 0, buffer.size)
            //Log.i(TAG,"record $ret");
            if (ret != null && ret > 0) {

                val samples = FloatArray(ret) { buffer[it] / 32768.0f }
                model.acceptSamples(samples)
                while (model.isReady()) {
                    model.decode()
                }
                //Log.i(TAG,"model ready");

                val isEndpoint = model.isEndpoint()
                val text = model.text.toLowerCase()
                var textToDisplay = lastText
                var insert = true

                if (isEndpoint) {
                    model.reset()
                    if (text.isNotBlank()) {
                        adapter.addItem("")

                    }
                }

                if (text.isNotBlank()) {
                    Log.i(TAG, "get $isEndpoint $text")
                    if (!lastText.equals(text))
                        adapter.updateItem(text)
                        recyclerView.post {
                            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                        }

                }

                lastText = text

            }
        }
    }

    private fun initMicrophone(): Boolean {
        Log.i(TAG, "initMicrophone")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            return false
        }

        val numBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
        Log.i(TAG,
            "buffer size in milliseconds: ${numBytes * 1000.0f / sampleRateInHz}")

        audioRecord = AudioRecord(
            audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            numBytes * 2 // a sample has two bytes as we are using 16-bit PCM
        )
        return true
    }

    private fun initModel() {
        Log.i(TAG, "initModel")
        val featConfig = getFeatureExtractorConfig(
            sampleRate = 16000.0f,
            featureDim = 80
        )
        //Please change the argument "type" if you use a different model
        val modelConfig = getModelConfig(type = 3, useGPU = useGPU)!!
        val decoderConfig = getDecoderConfig(method = "greedy_search", numActivePaths = 4)

        val config = RecognizerConfig(
            featConfig = featConfig,
            modelConfig = modelConfig,
            decoderConfig = decoderConfig,
            enableEndpoint = true,
            rule1MinTrailingSilence = 2.0f,
            rule2MinTrailingSilence = 0.8f,
            rule3MinUtteranceLength = 20.0f,
        )

        model = SherpaNcnn(
            assetManager = application.assets,
            config = config,
        )
    }

    private fun addText(text: String) {
        adapter.addItem(text)
        recyclerView.post {
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }
}
