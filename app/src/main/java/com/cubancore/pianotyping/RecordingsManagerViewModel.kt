package com.cubancore.pianotyping

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.cubancore.pianotyping.data.ActiveRecording
import com.cubancore.pianotyping.model.RecordingModel
import com.cubancore.pianotyping.repository.AppDatabase
import com.cubancore.pianotyping.repository.RecordingRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class RecordingsManagerViewModel(application: Application) : AndroidViewModel(application) {

    val database = AppDatabase.getDatabase(application)
    val repository = RecordingRepository(database.recordingDao())

    fun getRecordings(): LiveData<List<RecordingModel>> = repository.allRecordings

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

    fun stopRecording(title: String = "Untitled", compositor: String? = null): RecordingModel? {
        val recording = _currentRecording.value?.getRecording(title, compositor)
        _currentRecording.value = null
        if (recording != null) {
            repository.insert(recording)
        }
        return recording
    }

    fun updateRecordingMetadata(uuid: Uuid?, title: String, compositor: String?) {
        if (uuid != null) {
            repository.patch(uuid, title, compositor)
        }
    }
}