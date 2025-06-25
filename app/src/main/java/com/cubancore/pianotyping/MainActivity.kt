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
import com.cubancore.pianotyping.midiDriver.MidiDriver
import com.cubancore.pianotyping.midiDriver.MidiListener
import com.cubancore.pianotyping.ui.theme.PianoTypingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val midiDriver = MidiDriver(this)
        midiDriver.addListener(object : MidiListener {
            override fun onNoteOn(note: Int, velocity: Int) {
                super.onNoteOn(note, velocity)
                Log.d("DEBUG", "Note On  | Note: $note, Velocity: $velocity")
            }
            override fun onNoteOff(note: Int) {
                super.onNoteOff(note)
                Log.d("DEBUG", "Note Off | Note: $note")
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
}