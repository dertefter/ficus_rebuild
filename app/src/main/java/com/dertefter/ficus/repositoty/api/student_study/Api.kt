package com.dertefter.ficus.repositoty.api.student_study

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*



interface Api {
    @GET(".")
    suspend fun getBasePage(): Response<ResponseBody>

    @GET("timetable/timetable_lessons")
    suspend fun getTimetable(): Response<ResponseBody>

}