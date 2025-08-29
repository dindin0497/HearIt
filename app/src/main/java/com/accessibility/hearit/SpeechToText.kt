package com.accessibility.hearit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SpeechToText {
  val text: StateFlow<String>
  fun start()
  fun stop()
}

class RealSpeechToText(context: Context) : SpeechToText {
  override val text = MutableStateFlow("")

  val TAG="RealSpeechToText"


  private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
    setRecognitionListener(object : RecognitionListener {
      override fun onReadyForSpeech(p0: Bundle?) = Unit
      override fun onBeginningOfSpeech() = Unit
      override fun onRmsChanged(p0: Float) = Unit
      override fun onBufferReceived(p0: ByteArray?) = Unit
      override fun onEndOfSpeech() {
          Log.d(TAG, "onEndOfSpeech")
      }
      override fun onResults(results: Bundle?) {
        Log.d(TAG, "onResults")
        val partial = results
          ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
          ?.getOrNull(0) ?: ""
      }
      override fun onEvent(p0: Int, p1: Bundle?) = Unit

      override fun onPartialResults(results: Bundle?) {
        val partial = results
          ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        Log.d(TAG, "onPartialResult: $partial")
        //text.value = partial
      }

      override fun onError(error: Int) {
        val message = when (error) {
          SpeechRecognizer.ERROR_AUDIO -> "Audio"
          SpeechRecognizer.ERROR_CANNOT_CHECK_SUPPORT -> "Cannot Check Support"
          SpeechRecognizer.ERROR_CLIENT -> "Client"
          SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient Permissions"
          SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> "Language Not Supported"
          SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> "Language Unavailable"
          SpeechRecognizer.ERROR_NETWORK -> "Network"
          SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network Timeout"
          SpeechRecognizer.ERROR_NO_MATCH -> "No Match"
          SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Busy"
          SpeechRecognizer.ERROR_SERVER -> "Server Error"
          SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> "Server Disconnected"
          SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech Timeout"
          SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> "Too Many Requests"
          else -> "Unknown"
        }
        Log.e(TAG, "STT Error: $message")
      }
    })
  }
  private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
    putExtra(
      RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
    )
    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
    putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false)

    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 800)
    putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000)
  }


  override fun start() {
    speechRecognizer.startListening(intent)
  }

  override fun stop() {
    speechRecognizer.stopListening()
  }
}
