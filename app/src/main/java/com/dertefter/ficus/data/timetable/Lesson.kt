package com.dertefter.ficus.data.timetable
data class Lesson(
    val title: String,
    var time: String? = null,
    var type: String? = null,
    var aud: String? = null,
    var person: String? = null,
    var groups: String? = null
)