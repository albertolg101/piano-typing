package com.cubancore.pianotyping.midiDriver

import android.content.Context
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.media.midi.MidiReceiver
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

class MidiDriver(val context: Context) {
    private class CustomMidiReceiver(val listeners: MutableList<MidiListener>): MidiReceiver() {
        override fun onSend(msg: ByteArray?, offset: Int, count: Int, timestamp: Long) {
            // reference: https://learn.sparkfun.com/tutorials/midi-tutorial/all#messages
            if (msg != null) {
                var i = offset
                val end = offset + count

                while (i < end) {
                    val status = msg[i].toUByte().toInt()
                    when (status) {
                        // Channel message
                        in 0x80..0xEF -> {
                            val messageType = status and 0xF0
                            val channel = status and 0x0F
                            when (messageType) {
                                0x80, 0x90, 0xA0, 0xB0, 0xE0 -> {
                                    val data1 = msg[++i].toUByte().toInt()
                                    val data2 = msg[++i].toUByte().toInt()
                                    i++
                                    handleChannelMessage(messageType, channel, data1, data2, timestamp)
                                }
                                0xC0, 0xD0 -> {
                                    val data1 = msg[++i].toUByte().toInt()
                                    i++
                                    handleChannelMessage(messageType, channel, data1, null, timestamp)
                                }
                            }
                        }
                        // System exclusive message
                        0xF0 -> {
                            i++
                            while (i < end && msg[i].toUByte().toInt() != 0xF7) {
                                // TODO
                                i++
                            }
                            i++
                        }
                        // System common message
                        in 0xF1..0xF7 -> {
                            // TODO
                            i++
                        }
                        // System real time message
                        in 0xF8..0xFF -> {
                            // TODO
                            i++
                        }
                        // unknown status
                        else -> {
                            i++
                        }
                    }
                }
            }
        }

        fun handleChannelMessage(
            messageType: Int,
            channel: Int,
            data1: Int,
            data2: Int?,
            timestamp: Long
        ) {
            when (messageType) {
                // Note Off
                0x80 -> {
                    listeners.forEach { it.onNoteOff(data1) }
                }
                // Note On
                0x90 -> {
                    listeners.forEach { it.onNoteOn(data1, data2 ?: 0) }
                }
            }
        }
    }

    private class CustomDeviceCallback(
        val midiManager: MidiManager,
        val context: Context,
        val midiReceiver: CustomMidiReceiver,
    ) : MidiManager.DeviceCallback() {
        fun onDeviceAdded(device: MidiDeviceInfo?, showMessage: Boolean) {
            if (device != null) {
                // Show message when MIDI device is connected
                if (showMessage) {
                    Toast.makeText(
                        context,
                        "Connected MIDI device: ${device.id}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Start Listener
                midiManager.openDevice(
                    device,
                    MidiManager.OnDeviceOpenedListener { device ->
                        device.info.ports
                            .filter { it.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }
                            .forEach {
                                device.openOutputPort(it.portNumber).connect(midiReceiver)
                            }
                    },
                    Handler(Looper.getMainLooper())
                )
            }
        }

        override fun onDeviceAdded(device: MidiDeviceInfo?) {
            super.onDeviceRemoved(device)
            onDeviceAdded(device, true)
        }

        override fun onDeviceRemoved(device: MidiDeviceInfo?) {
            super.onDeviceRemoved(device)

            if (device != null) {
                // Show message when MIDI device is disconnected
                Toast.makeText(
                    context,
                    "Disconnected MIDI device: ${device.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private val listeners: MutableList<MidiListener> = mutableListOf()

    private val midiManager: MidiManager =
        context.getSystemService(Context.MIDI_SERVICE) as MidiManager
    // Used when a midi event is received
    private val midiReceiver = CustomMidiReceiver(listeners)
    // Used when a device is connected or disconnected
    private val midiDeviceCallback = CustomDeviceCallback(midiManager, context, midiReceiver)

    init {
        // Since my target is MIDI 1.0,
        // I'm using deprecated methods in order to support a wider range of Android API versions
        midiManager.devices.forEach { device ->
            midiDeviceCallback.onDeviceAdded(device, false)
        }
        midiManager.registerDeviceCallback(midiDeviceCallback, Handler(Looper.getMainLooper()))
    }

    fun addListener(listener: MidiListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: MidiListener) {
        listeners.remove(listener)
    }

    val numberOfDevices: Int
        get() {
            return midiManager.devices.size
        }

}