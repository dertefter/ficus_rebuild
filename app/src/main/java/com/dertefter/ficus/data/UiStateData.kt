package com.dertefter.ficus.data

data class UiStateData (
    var isAuthrized: Boolean? = false,
    var User: User? = null,
    var timetableData: TimetableData? = TimetableData(),
    var authError: Boolean = false,
)