package com.dertefter.ficus.data.timetable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weeks")
data class Week(
    @ColumnInfo(name = "title")
    val title: String,

    @PrimaryKey
    val weekQuery: String,
    @ColumnInfo(name = "today")
    var today: Boolean = false,
    @ColumnInfo(name = "timetable")
    var timetable: Timetable? = null,

): Parcelable