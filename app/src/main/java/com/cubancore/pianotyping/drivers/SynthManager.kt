package com.cubancore.pianotyping.drivers

import android.content.Context
import com.robsonmartins.androidmidisynth.SynthManager

class SynthDriver: SynthManager {
    constructor(context: Context): super(context)

    constructor(context: Context, soundfontPath: String, volume: Int = 127) : this(context) {
        loadSF(soundfontPath)
        setVolume(volume)
    }

    /**
     * @brief   Plays the note.
     * @param   note      The note to be played.
     * @param   velocity  The velocity of the note to be played.
     */
    fun noteOn(note: Int, velocity: Int) {
        fluidsynthNoteOn(note, velocity)
    }

    /**
     * @brief   Import of the native implementation of SynthManager.fluidsynthNoteOff() method.
     * @details Stops the playing note.
     * @param   note The note to be stopped.
     */
    fun noteOff(note: Int) {
        fluidsynthNoteOff(note)
    }
}