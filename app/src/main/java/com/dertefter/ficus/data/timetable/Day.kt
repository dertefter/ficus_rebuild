package com.dertefter.ficus.data.timetable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Day(
    val title: String,
    var date: String?,
    var lessons: List<Lesson>? = null,
    var today: Boolean = false
): Parcelable