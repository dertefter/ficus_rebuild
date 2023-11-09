package com.dertefter.ficus.data.timetable

import retrofit2.http.Query

data class Timetable(
    var weeks: List<Week>? = null,
    var days: List<Days>? = null
)