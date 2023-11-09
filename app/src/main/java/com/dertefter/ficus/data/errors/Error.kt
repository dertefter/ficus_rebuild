package com.dertefter.ficus.data.errors
data class Error(
    val title: String? = null,
    val text: String? = null,
    val stackTrace: String? = null
)