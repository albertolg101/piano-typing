package com.cubancore.pianotyping

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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
        init { System.loadLibrary("synth-lib") }
    }

    private lateinit var midiDriver: MidiDriver
    private lateinit var synthDriver: SynthDriver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        midiDriver = MidiDriver(this)
        synthDriver = SynthDriver(this, "AcousticGrandPiano.sf2")

        midiDriver.addListener(object : MidiListener {
            override fun onNoteOn(note: Int, velocity: Int) {
                super.onNoteOn(note, velocity)
                Log.d("DEBUG", "Note On  | Note: $note, Velocity: $velocity")
                synthDriver.noteOn(note, velocity)
            }
            override fun onNoteOff(note: Int) {
                super.onNoteOff(note)
                Log.d("DEBUG", "Note Off | Note: $note")
                synthDriver.noteOff(note)
            }
        })



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
                        MainScreen(synthDriver)
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
        if (insetsController != null) {
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        }
    }
}