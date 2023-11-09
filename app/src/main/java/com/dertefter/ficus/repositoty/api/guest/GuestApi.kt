package com.dertefter.ficus.repositoty.api.guest

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*



interface GuestApi {
    @GET(".")
    suspend fun getBasePage(): Response<ResponseBody>

    @GET(".")
    suspend fun getNews(@Query("main_events") page: String?): Response<ResponseBody>

    @GET("news/news_more")
    suspend fun getNewsMore(@Query("idnews") idnews: String?): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes")
    suspend fun getGroupList(@Query("query") group: String): Response<ResponseBody>

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getTimetable(@Query("group") group: String, @Query("group") week: String?): Response<ResponseBody>
}