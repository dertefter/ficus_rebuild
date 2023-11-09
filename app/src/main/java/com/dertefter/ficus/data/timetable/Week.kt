package com.dertefter.ficus.data.timetable

import retrofit2.http.Query

data class Week(
    val title: String,
    var today: Boolean = false,
    val query: String,
    var timetable: Timetable? = null
)