package com.cubancore.pianotyping.data

import android.util.Log
import com.cubancore.pianotyping.model.RecordingModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
class ActiveRecording {
    private val _startedAt: Long = Clock.System.now().toEpochMilliseconds()
    private val _recording: MutableList<Record> = mutableListOf()
    private var _pausedAt: Long? = null
    private var _offset: Long = 0

    val startedAt: Long
        get() = _startedAt

    val pausedAt: Long?
        get() = _pausedAt

    val elapsed: Long
        get() = (pausedAt ?: Clock.System.now().toEpochMilliseconds()) - _startedAt - _offset

    fun addRecord(note: Int, velocity: Int) {
        if (pausedAt == null) {
            Log.d("DEBUG", "RECORDING: note = $note velocity = $velocity")
            _recording.add(
                Record(
                    note = note,
                    velocity = velocity,
                    timestamp = elapsed
                )
            )
        }
    }

    fun pause() {
        val now = Clock.System.now().toEpochMilliseconds()
        if (_pausedAt == null) {
            _pausedAt = now
        }
    }

    fun resume() {
        val now = Clock.System.now().toEpochMilliseconds()
        _offset += now - (_pausedAt ?: now)
        _pausedAt = null
    }

    fun getRecording(title: String, compositor: String?): RecordingModel {
        return RecordingModel(
            uuid = Uuid.random(),
            title = title,
            compositor = compositor,
            records = _recording.toList()
        )
    }
}
