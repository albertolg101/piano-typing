package com.cubancore.pianotyping.data.converters

import androidx.room.TypeConverter
import com.cubancore.pianotyping.data.Records
import kotlinx.serialization.json.Json

class RecordsConverter {

    @TypeConverter
    fun fromRecords(records: Records): String {
        return Json.encodeToString(records)
    }

    @TypeConverter
    fun toRecords(value: String): Records {
        return Json.decodeFromString(value)
    }
}