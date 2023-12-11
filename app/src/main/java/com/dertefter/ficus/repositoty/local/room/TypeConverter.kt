package com.dertefter.ficus.repositoty.local.room

import com.dertefter.ficus.data.timetable.Timetable
import com.google.gson.Gson
import androidx.room.TypeConverter
class TypeConverter {
    @TypeConverter
    fun fromTimetable(timetable: Timetable?): String? {
        return Gson().toJson(timetable)
    }

    @TypeConverter
    fun toTimetable(timetableJson: String?): Timetable? {
        return Gson().fromJson(timetableJson, Timetable::class.java)
    }
}