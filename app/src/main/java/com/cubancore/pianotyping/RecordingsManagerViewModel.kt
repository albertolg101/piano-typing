package com.cubancore.pianotyping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.cubancore.pianotyping.data.ActiveRecording
import com.cubancore.pianotyping.data.Recording

class RecordingsManagerViewModel : ViewModel() {
    private val _recordings: MutableLiveData<MutableList<Recording>> =
        MutableLiveData(mutableListOf())
    val recordings: LiveData<List<Recording>> = _recordings.map { it.toList() }

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

    fun stopRecording(title: String = "Untitled", compositor: String? = null) {
        val recording =_currentRecording.value?.getRecording(title, compositor)
        _currentRecording.value = null
        if (recording != null) {
            _recordings.value?.add(recording)
        }
    }
}