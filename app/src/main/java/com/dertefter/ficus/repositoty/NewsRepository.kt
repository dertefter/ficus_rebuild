package com.dertefter.ficus.repositoty

import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.utils.netiCore.ResponseParser

class NewsRepository {
    private val api: GuestApi = NetworkService.retrofitService()
    private val allNewsData = mutableListOf<NewsItem>()

    suspend fun getNews(page: Int, isNeedToReplaceData: Boolean): List<NewsItem>? {
        try {
            val response = api.getNews(page.toString())
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseNews(response.body())
                if (isNeedToReplaceData || allNewsData.isEmpty()) {
                    allNewsData.clear()
                }
                if (parsedData != null) {
                    allNewsData.addAll(parsedData)
                }
                return allNewsData
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}