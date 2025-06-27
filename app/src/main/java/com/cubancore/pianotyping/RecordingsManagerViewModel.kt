package com.cubancore.pianotyping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.cubancore.pianotyping.data.ActiveRecording
import com.cubancore.pianotyping.data.Recording
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class RecordingsManagerViewModel : ViewModel() {
    private val _recordings: MutableLiveData<MutableMap<Uuid, Recording>> =
        MutableLiveData(mutableMapOf())
    val recordings: LiveData<Map<Uuid, Recording>> = _recordings.map { it.toMap() }

    private val _currentRecording: MutableLiveData<ActiveRecording?> = MutableLiveData()
    val currentRecording: LiveData<ActiveRecording?> = _currentRecording
    val isRecording: LiveData<Boolean> = _currentRecording.map { it != null }

    fun startRecording() {
        _currentRecording.value = ActiveRecording()
    }

    fun resumeRecording() {
        _currentRecording.value?.resume()
    }

    fun pauseRecording() {
        _currentRecording.value?.pause()
    }

    fun stopRecording(title: String = "Untitled", compositor: String? = null): Recording? {
        val recording =_currentRecording.value?.getRecording(title, compositor)
        _currentRecording.value = null
        if (recording != null) {
            _recordings.value?.set(recording.uuid, recording)
        }
        return recording
    }

    fun updateRecordingMetadata(uuid: Uuid?, title: String?, compositor: String?) {
        val recording = _recordings.value?.get(uuid)

        if (recording != null) {
            recording.title = title ?: recording.title
            recording.compositor = compositor ?: recording.compositor
        }
    }
}