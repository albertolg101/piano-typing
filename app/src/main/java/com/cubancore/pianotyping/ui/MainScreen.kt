package com.cubancore.pianotyping.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.cubancore.pianotyping.drivers.SynthDriver


@Composable
fun MainScreen(
    synthDriver: SynthDriver
) {
    Column {
        Surface {
            Text("KeyBoard App")
        }
        Piano(
            onNoteOn = synthDriver::noteOn
        )
    }
}