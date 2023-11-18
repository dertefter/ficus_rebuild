package com.dertefter.ficus.data.timetable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Timetable(
    var days: List<Day>? = null
): Parcelable