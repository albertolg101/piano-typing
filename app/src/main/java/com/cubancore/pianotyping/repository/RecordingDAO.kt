package com.cubancore.pianotyping.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cubancore.pianotyping.model.RecordingModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Dao
@OptIn(ExperimentalUuidApi::class)
interface RecordingDAO {

    @Query("SELECT * FROM recordings ORDER BY title ASC")
    fun getAll(): LiveData<List<RecordingModel>>

    @Query("SELECT * FROM recordings WHERE uuid = :uuid LIMIT 1")
    fun get(uuid: Uuid): LiveData<RecordingModel?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recording: RecordingModel)

    @Query("UPDATE recordings SET title = :title, compositor = :compositor WHERE uuid = :uuid")
    fun patch(uuid: Uuid, title: String, compositor: String?)

    @Update
    fun update(recording: RecordingModel)

    @Delete
    fun delete(recording: RecordingModel)
}