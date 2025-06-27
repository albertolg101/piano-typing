package com.cubancore.pianotyping.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cubancore.pianotyping.data.converters.RecordsConverter
import com.cubancore.pianotyping.data.converters.UuidConverter
import com.cubancore.pianotyping.model.RecordingModel
import java.util.concurrent.Executors

@Database(entities = [RecordingModel::class], version = 1, exportSchema = false)
@TypeConverters(UuidConverter::class, RecordsConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun recordingDao(): RecordingDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val databaseWriteExecutor = Executors.newFixedThreadPool(2)

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room
                    .databaseBuilder(context, AppDatabase::class.java, "piano_typing_db")
//                    .setQueryCallback(
//                        RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
//                            Log.d("DEBUG", "SQL: $sqlQuery\nARGS: $bindArgs")
//                        },
//                        Executors.newSingleThreadExecutor()
//                    )
//                    .enableMultiInstanceInvalidation()
                    .build()
                INSTANCE = db
                db
            }
        }
    }
}