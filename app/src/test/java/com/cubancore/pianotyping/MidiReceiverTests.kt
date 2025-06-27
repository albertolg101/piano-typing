package com.cubancore.pianotyping

import com.cubancore.pianotyping.drivers.MidiDriver
import com.cubancore.pianotyping.drivers.MidiListener
import junit.framework.TestCase.assertEquals
import org.junit.Test

fun List<Int>.toByteArray() = ByteArray(this.size) { pos -> this[pos].toByte() }

class MidiReceiverTests {
    @Test
    fun noteOn_IsCorrect() {
        val sentNote = 42
        val sentVelocity = 101
        var receivedNote: Int? = null
        var receivedVelocity: Int? = null

        val midiReceiver = MidiDriver.CustomMidiReceiver(mutableListOf(
            object: MidiListener {
                override fun onNoteOn(note: Int, velocity: Int) {
                    super.onNoteOn(note, velocity)
                    receivedNote = note
                    receivedVelocity = velocity
                }
            }
        ))

        val message = listOf(0x01, 0x90, sentNote, sentVelocity).toByteArray()
        midiReceiver.onSend(message, 1, 3, 0)

        assertEquals(sentNote, receivedNote)
        assertEquals(sentVelocity, receivedVelocity)
    }

    @Test
    fun noteOff_IsCorrect() {
        val sentNote = 42
        var receivedNote: Int? = null

        val midiReceiver = MidiDriver.CustomMidiReceiver(mutableListOf(
            object: MidiListener {
                override fun onNoteOff(note: Int) {
                    super.onNoteOff(note)
                    receivedNote = note
                }
            }
        ))

        val message = listOf(0x01, 0x80, sentNote, 0).toByteArray()
        midiReceiver.onSend(message, 1, 3, 0)

        assertEquals(sentNote, receivedNote)
    }
}