package com.dertefter.ficus.data

data class UiStateData (
    var isAuthrized: Boolean? = false,
    var name: String = "Гость",
    var user: User? = null,
    var individualTimetable: Boolean = false,
    var groupTitle: String = ""
)