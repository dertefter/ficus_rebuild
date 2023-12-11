package com.dertefter.ficus.repositoty.api.student_study

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*



interface AuthApi {
    @GET(".")
    suspend fun getBasePage(): Response<ResponseBody>

    @POST("ssoservice/json/authenticate")
    suspend fun authPart1(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("json/ido/users?_action=idFromSession")
    suspend fun authPart2(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET(".")
    suspend fun authPart3(): Response<ResponseBody>

    @POST("json/users?_action=validateGoto")
    suspend fun authPart4(@Query("_action") action: String?, @Body requestBody: RequestBody): Response<ResponseBody>

    @GET("timetable/timetable_lessons")
    suspend fun getIndividualTimetable(): Response<ResponseBody>

    @GET("mess_teacher")
    suspend fun getChatLists(@Query("year") year: String?): Response<ResponseBody>

    @GET("mess_teacher/view")
    suspend fun loadMessage(@Query("id") id: String?): Response<ResponseBody>

}