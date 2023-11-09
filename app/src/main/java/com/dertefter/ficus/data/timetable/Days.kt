package com.dertefter.ficus.data.timetable
data class Days(
    val date: String,
    var lessons: List<Lesson>? = null
)