package com.cubancore.pianotyping.drivers

interface MidiListener {
    fun onNoteOn(note: Int, velocity: Int) {}

    fun onNoteOff(note: Int) {}
}
