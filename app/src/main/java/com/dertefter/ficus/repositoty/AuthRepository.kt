package com.dertefter.ficus.repositoty

import android.util.Log
import com.dertefter.ficus.data.User
import com.dertefter.ficus.repositoty.api.student_study.AuthApi
import com.dertefter.ficus.repositoty.api.student_study.AuthNetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.utils.netiCore.ResponseParser
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class AuthRepository {
    var authApi = AuthNetworkService.retrofitService()
    val appPreferences = AppPreferences
    suspend fun tryAuth(login: String, password: String): Boolean {
        val url1 = "https://login.nstu.ru/"
        try {
            val retrofit = Retrofit.Builder().baseUrl(url1).build()
            val service = retrofit.create(AuthApi::class.java)
            val responseBodyPt1Json =
                "{\"authId\":\"eyAidHlwIjogIkpXVCIsICJhbGciOiAiSFMyNTYiIH0.eyAib3RrIjogInR2ZW9yazY0dHU5aDc5dTRtb2xoZTBrb3NkIiwgInJlYWxtIjogIm89bG9naW4sb3U9c2VydmljZXMsZGM9b3BlbmFtLGRjPWNpdSxkYz1uc3R1LGRjPXJ1IiwgInNlc3Npb25JZCI6ICJBUUlDNXdNMkxZNFNmY3dIV1l6elZqbTdlbjREYXptS2ZfQktXLTA0UGR1M0lMay4qQUFKVFNRQUNNRElBQWxOTEFCTTJNamc0T0RrM05qUXpNVFE1TXpJMk56TTUqIiB9.iQ7F98fLLFrcDlSI5kYU14d9_Dg9lKN5meoGYIdXxcA\",\"template\":\"\",\"stage\":\"JDBCExt1\",\"header\":\"Авторизация\",\"callbacks\":[{\"type\":\"NameCallback\",\"output\":[{\"name\":\"prompt\",\"value\":\"Логин:\"}],\"input\":[{\"name\":\"IDToken1\",\"value\":\"$login\"}]},{\"type\":\"PasswordCallback\",\"output\":[{\"name\":\"prompt\",\"value\":\"Пароль:\"}],\"input\":[{\"name\":\"IDToken2\",\"value\":\"$password\"}]}]}"
            val requestBodyPt1 = responseBodyPt1Json.toRequestBody("application/json".toMediaTypeOrNull())
            val response = service.authPart1(requestBodyPt1)
            return if (response.isSuccessful) {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                val Jobject = JSONObject(prettyJson)
                val tokenId = Jobject.getString("tokenId").toString()
                appPreferences.token = tokenId
                appPreferences.login = login
                appPreferences.password = password
                appPreferences.group = "individual"
                authApi = AuthNetworkService.retrofitService(tokenId)
                updateProfileData()
                true
            }else{
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun updateProfileData(): User?{
        try {
            val response = authApi.getBasePage()
            if (response.isSuccessful) {
                val user = ResponseParser().parseProfileData(response.body())
                user?.login = appPreferences.login.toString()
                return user
            }
        }catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }

}