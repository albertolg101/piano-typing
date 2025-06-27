package com.cubancore.pianotyping.extensions

import android.text.format.DateUtils

fun Long.toDurationString() = DateUtils.formatElapsedTime(this / 1000)