package com.cubancore.pianotyping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.cubancore.pianotyping.extensions.toInt

@Composable
fun Piano(
    visibleKeys: Int = 10,
    onNoteOn: Function2<Int, Int, Unit>?
) {
    BoxWithConstraints (
        modifier = Modifier.fillMaxSize()
    ) {
        val whiteKeyWidth = this.maxWidth / visibleKeys
        val blackKeyWidth = (this.maxWidth.value / (visibleKeys - 1) / 1.8).dp
        val blackKeyHeight = (this.maxHeight.value / 1.6).dp

        Row {
            repeat(visibleKeys) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(whiteKeyWidth)
                        .padding(horizontal = 1.dp)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .indication(interactionSource, ripple())
                            .pointerInput(Unit) {
                                if (onNoteOn != null) {
                                    detectTapGestures(
                                        onPress = { offset ->
                                            val press = PressInteraction.Press(offset)
                                            interactionSource.emit(press)
                                            val note = 60 + index / 7 * 12 + (index % 7) * 2 - ((index % 7) > 2).toInt()
                                            onNoteOn(note, 127)

                                            tryAwaitRelease()
                                            interactionSource.emit(PressInteraction.Release(press))
                                            onNoteOn(note, 0)
                                        },
                                    )
                                }
                            }
                    )
                }
            }
        }
        Row (
            horizontalArrangement = Arrangement.spacedBy(whiteKeyWidth - blackKeyWidth),
            modifier = Modifier.padding(horizontal = whiteKeyWidth - blackKeyWidth / 2)
        ) {
            repeat(visibleKeys - 1) { index ->
                Box(
                    modifier = Modifier
                        .size(width = blackKeyWidth, height = blackKeyHeight)
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    if (index % 7 != 2 && index % 7 != 6) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                                .indication(interactionSource, ripple(color = Color.White))
                                .pointerInput(Unit) {
                                    if (onNoteOn != null) {
                                        detectTapGestures(
                                            onPress = { offset ->
                                                val press = PressInteraction.Press(offset)
                                                interactionSource.emit(press)
                                                val note = 60 + index / 7 * 12 + (index % 7) * 2 + 1 - ((index % 7) > 2).toInt()
                                                onNoteOn(note, 127)

                                                tryAwaitRelease()
                                                interactionSource.emit(PressInteraction.Release(press))
                                                onNoteOn(note, 0)
                                            },
                                        )
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}