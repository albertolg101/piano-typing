package com.cubancore.pianotyping.ui

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cubancore.pianotyping.R
import com.cubancore.pianotyping.RecordingsManagerViewModel
import com.cubancore.pianotyping.data.Recording
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
@OptIn(kotlin.uuid.ExperimentalUuidApi::class)
fun MainScreen(
    onNoteOn: (Int, Int) -> Unit,
    recordingsManager: RecordingsManagerViewModel
) {
    val recordingToEdit: MutableState<Recording?> = rememberSaveable { mutableStateOf(null) }

    if (recordingToEdit.value != null) {
        EditRecordingDialog(
            title = "",
            compositor = null,
            onSaveRequest = { title, compositor ->
                recordingsManager.updateRecordingMetadata(
                    uuid = recordingToEdit.value?.uuid,
                    title = title,
                    compositor = compositor,
                )
                recordingToEdit.value = null
            },
            onDismissRequest = { recordingToEdit.value = null }
        )
    }

    Column {
        Surface (
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val isRecording by recordingsManager.isRecording.observeAsState(false)
                val currentRecording by recordingsManager.currentRecording.observeAsState(null)

                Row (
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    if (!isRecording) {
                        Button (
                            onClick = { recordingsManager.startRecording() },
                            modifier = Modifier.size(35.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) { }
                    } else {
                        if (currentRecording?.pausedAt == null) {
                            Button(
                                onClick = { recordingsManager.pauseRecording() },
                                modifier = Modifier.size(35.dp),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.round_pause_24),
                                    contentDescription = stringResource(R.string.pause_recording)
                                )
                            }
                        } else {
                            Button (
                                onClick = { recordingsManager.resumeRecording() },
                                modifier = Modifier.size(35.dp),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Green,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.round_play_arrow_24),
                                    contentDescription = stringResource(R.string.resume_recording)
                                )
                            }
                        }
                        Button (
                            onClick = {
                               recordingToEdit.value = recordingsManager.stopRecording()
                            },
                            modifier = Modifier.size(35.dp),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.round_stop_24),
                                contentDescription = stringResource(R.string.stop_recording)
                            )
                        }
                    }
                }

                if (isRecording) {
                    val elapsed by produceState(initialValue = 0L) {
                        while (isActive) {
                            delay(1_000L)
                            value = currentRecording?.elapsed ?: 0
                        }
                    }
                    val elapsedString = DateUtils.formatElapsedTime(elapsed / 1000)
                    Text(
                        text = "${stringResource(R.string.recording)}: $elapsedString",
                        fontSize = 20.sp
                    )
                }

                // Recordings
                Button (
                    onClick = { }
                ) {
                    Text(text = stringResource(R.string.records))
                }
            }
        }
        Piano(
            onNoteOn = onNoteOn
        )
    }
}