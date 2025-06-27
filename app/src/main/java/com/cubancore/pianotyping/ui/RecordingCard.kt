package com.cubancore.pianotyping.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cubancore.pianotyping.R
import com.cubancore.pianotyping.extensions.toDurationString
import com.cubancore.pianotyping.model.RecordingModel

@Composable
fun RecordingCard(recording: RecordingModel, isSelected: Boolean) {
    val compositor = recording.compositor ?: stringResource(R.string.unknown_compositor)
    val duration = recording.records.maxOf { it.timestamp } .toDurationString()

    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = recording.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "By: $compositor",
                        color = Color.Gray
                    )
                }

                Column {
                    Text(
                        text = duration,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            if (isSelected) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                ) {
                    Row {
                        Button (
                            onClick = {  },
                            modifier = Modifier.size(35.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.round_play_arrow_24),
                                contentDescription = stringResource(R.string.resume_recording)
                            )
                        }
                    }
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        IconButton (
                            onClick = {},
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.round_cloud_upload_24),
                                contentDescription = stringResource(R.string.upload_recording),
                            )
                        }
                        IconButton (
                            onClick = {},
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_recording_metadata),
                            )
                        }
                        IconButton (
                            onClick = {},
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_recording),
                            )
                        }
                    }
                }
            }
        }
    }
}