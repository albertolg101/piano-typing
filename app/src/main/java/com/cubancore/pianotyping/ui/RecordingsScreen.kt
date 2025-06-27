package com.cubancore.pianotyping.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cubancore.pianotyping.RecordingsManagerViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
@OptIn(ExperimentalUuidApi::class)
fun RecordingsScreen(recordingsManager: RecordingsManagerViewModel) {
    val recordings by recordingsManager.getRecordings().observeAsState(initial = emptyList())

    if (recordings.isEmpty()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No recordings")
        }
    } else {
        val selectedItemUuid: MutableState<Uuid?> = rememberSaveable { mutableStateOf(null) }
        Log.d("DEBUG", selectedItemUuid.value.toString())
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(recordings) { recording ->
                Box (
                    modifier = Modifier.clickable { selectedItemUuid.value = recording.uuid }
                ) {
                    RecordingCard(
                        recording = recording,
                        isSelected = selectedItemUuid.value == recording.uuid,
                        onPatch = { title, compositor ->
                            recordingsManager.updateRecordingMetadata(
                                uuid = recording.uuid,
                                title = title,
                                compositor = compositor
                            )
                        }
                    )
                }
            }
        }
    }
}