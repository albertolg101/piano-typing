package com.cubancore.pianotyping.repository

import androidx.lifecycle.LiveData
import com.cubancore.pianotyping.model.RecordingModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class RecordingRepository(private val recordingDao: RecordingDAO) {
    val allRecordings: LiveData<List<RecordingModel>> = recordingDao.getAll()

    fun get(uuid: Uuid) = recordingDao.get(uuid)

    fun insert(recording: RecordingModel) {
        AppDatabase.databaseWriteExecutor.execute {
            recordingDao.insert(recording)
        }
    }

    fun patch(uuid: Uuid, title: String, compositor: String?) {
        AppDatabase.databaseWriteExecutor.execute {
            recordingDao.patch(uuid, title, compositor)
        }
    }

    fun update(recording: RecordingModel) {
        AppDatabase.databaseWriteExecutor.execute {
            recordingDao.update(recording)
        }
    }

    fun delete(recording: RecordingModel) {
        AppDatabase.databaseWriteExecutor.execute {
            recordingDao.delete(recording)
        }
    }
}