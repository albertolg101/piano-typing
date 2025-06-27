package com.cubancore.pianotyping

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cubancore.pianotyping.drivers.MidiDriver
import com.cubancore.pianotyping.drivers.MidiListener
import com.cubancore.pianotyping.drivers.SynthDriver
import com.cubancore.pianotyping.ui.MainScreen
import com.cubancore.pianotyping.ui.theme.PianoTypingTheme

class MainActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("synth-lib")
        }
    }

    private lateinit var midiDriver: MidiDriver
    private lateinit var synthDriver: SynthDriver
    private val recordingsManager: RecordingsManagerViewModel by viewModels()
    private lateinit var mainMidiListener: MainMidiListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        midiDriver = MidiDriver(this)
        synthDriver = SynthDriver(this, "AcousticGrandPiano.sf2")
        mainMidiListener = MainMidiListener(synthDriver, recordingsManager)
        midiDriver.addListener(mainMidiListener)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        enableEdgeToEdge()
        setFullScreen()

        setContent {
            PianoTypingTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .displayCutoutPadding()
                    ) {
                        MainScreen(
                            onNoteOn = mainMidiListener::onNoteOn,
                            onRecordingsOpen = {
                                val intent = Intent(this@MainActivity, RecordingsActivity::class.java)
                                startActivity(intent)
                            },
                            recordingsManager = recordingsManager
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        synthDriver.finalize()
    }

    private fun setFullScreen() {
        val insetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}

class MainMidiListener(
    private val synthDriver: SynthDriver,
    private val recordingsManager: RecordingsManagerViewModel
) : MidiListener {
    override fun onNoteOn(note: Int, velocity: Int) {
        super.onNoteOn(note, velocity)
        Log.d("DEBUG", "Note On  | Note: $note, Velocity: $velocity")
        synthDriver.noteOn(note, velocity)
        if (recordingsManager.isRecording.value ?: false) {
            recordingsManager.currentRecording.value!!.addRecord(note, velocity)
        }
    }

    override fun onNoteOff(note: Int) {
        super.onNoteOff(note)
        Log.d("DEBUG", "Note Off | Note: $note")
        synthDriver.noteOff(note)
        if (recordingsManager.isRecording.value ?: false) {
            recordingsManager.currentRecording.value!!.addRecord(note, 0)
        }
    }
}
