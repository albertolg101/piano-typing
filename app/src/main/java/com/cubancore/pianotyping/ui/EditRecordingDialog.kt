package com.cubancore.pianotyping.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cubancore.pianotyping.R

@Composable
fun EditRecordingDialog(
    title: String?,
    compositor: String?,
    onSaveRequest: (title: String, compositor: String?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val title = rememberSaveable { mutableStateOf(title ?: "") }
    val compositor = rememberSaveable { mutableStateOf(compositor ?: "") }
    @StringRes val error: MutableState<Int?> = rememberSaveable { mutableStateOf(null) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 4.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.round_save_24),
                    contentDescription = stringResource(R.string.save_recording),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "${stringResource(R.string.save_recording)}: ",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.value != null) {
                    Text(
                        text = stringResource(error.value!!),
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = {
                        Text(text = "${stringResource(R.string.recording_title)}*")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = compositor.value,
                    onValueChange = { compositor.value = it },
                    label = {
                        Text(text = stringResource(R.string.recording_compositor))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp, 16.dp, 0.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                if (title.value == "") {
                                    error.value = R.string.error_recording_empty_value
                                }

                                if (title.value != "") {
                                    onSaveRequest(
                                        title.value,
                                        if (compositor.value == "") null else compositor.value
                                    )
                                }
                            },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = stringResource(R.string.save))
                        }
                        TextButton(
                            onClick = onDismissRequest,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                }
            }
        }
    }
}