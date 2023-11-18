package com.dertefter.ficus.data.timetable

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Week(
    val title: String,
    val query: String,
    var today: Boolean = false,
    var timetable: Timetable? = null
): Parcelable