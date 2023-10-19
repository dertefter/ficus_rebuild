package com.dertefter.ficus.repositoty.api.guest

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*



interface GuestApi {
    @GET(".")
    suspend fun getBasePage(): Response<ResponseBody>

    @GET(".")
    suspend fun getNews(@Query("main_events") page: String?): Response<ResponseBody>
}