package com.dertefter.ficus.data.timetable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.parcelize.Parcelize

@Parcelize
data class Timetable(
    var days: List<Day>? = null
): Parcelable