package com.dertefter.ficus.data.news

data class NewsItem(
    val newsid: String,
    val title: String,
    val date: String,
    val imageUrl: String?,
    val tag: String,
    val type: String,
    var error: String? = null
)