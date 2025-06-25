package com.cubancore.pianotyping.midiDriver

interface MidiListener {
    fun onNoteOn(note: Int, velocity: Int) {}

    fun onNoteOff(note: Int) {}
}
