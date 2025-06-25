package com.cubancore.pianotyping

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.cubancore.pianotyping.drivers.MidiDriver
import com.cubancore.pianotyping.drivers.MidiListener
import com.cubancore.pianotyping.drivers.SynthDriver
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
        synthDriver = SynthDriver(this, "AcousticGrandPiano.sf2", 127)

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

        enableEdgeToEdge()
        setContent {
            PianoTypingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Text(
                            text = "Android",
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        synthDriver.finalize()
        super.onDestroy()
    }
}