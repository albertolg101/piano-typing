package com.cubancore.pianotyping.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cubancore.pianotyping.data.Records
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "recordings")
@OptIn(ExperimentalUuidApi::class)
data class RecordingModel (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("uuid")
    val uuid: Uuid,
    @ColumnInfo("title")
    var title: String,
    @ColumnInfo("compositor")
    var compositor: String?,
    @ColumnInfo("records")
    val records: Records,
)