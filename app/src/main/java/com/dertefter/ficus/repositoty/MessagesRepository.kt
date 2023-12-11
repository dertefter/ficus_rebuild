package com.dertefter.ficus.repositoty

import android.util.Log
import com.dertefter.ficus.data.messages.DiSpaceMessage
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.data.messages.StudentStudyMessage
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.repositoty.api.student_study.AuthApi
import com.dertefter.ficus.repositoty.api.student_study.AuthNetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.utils.netiCore.ResponseParser

class MessagesRepository {
    private val appPreferences = AppPreferences
    private val authApi: AuthApi = AuthNetworkService.retrofitService()

    private var diSpaceChatItemList = mutableListOf<DiSpaceMessage>()
    private var teacherChatItemList = mutableListOf<StudentStudyChatItem>()
    private var otherChatItemList = mutableListOf<StudentStudyChatItem>()

    fun getTeacherChatList(): List<StudentStudyChatItem> {
        return teacherChatItemList
    }
    suspend fun updateTeacherChatList(): List<StudentStudyChatItem> {
        try{
            val response = authApi.getChatLists("-1")
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseChatLists(response.body(), "teacher")
                Log.e("MessagesRepository", "updateMainChatLists: ${parsedData}")
                if (parsedData.isNotEmpty()) {
                    teacherChatItemList.clear()
                    teacherChatItemList.addAll(parsedData.toMutableList())
                    return getTeacherChatList()
                }
            }
        }catch (e: Exception){
            Log.e("MessagesRepository", "updateMainChatLists: ${e.stackTraceToString()}")
        }
        return getTeacherChatList()

    }

    fun getOtherChatList(): List<StudentStudyChatItem> {
        return otherChatItemList
    }

    suspend fun updateOtherChatList(): List<StudentStudyChatItem> {
        try{
            val response = authApi.getChatLists("-1")
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseChatLists(response.body(), "other")
                Log.e("MessagesRepository", "updateMainChatLists: ${parsedData}")
                if (parsedData.isNotEmpty()) {
                    teacherChatItemList.clear()
                    teacherChatItemList.addAll(parsedData.toMutableList())
                    return getTeacherChatList()
                }
            }
        }catch (e: Exception){
            Log.e("MessagesRepository", "updateMainChatLists: ${e.stackTraceToString()}")
        }
        return getTeacherChatList()

    }

    suspend fun loadMessage(MessageID: String): String? {
        try {
            val response = authApi.loadMessage(MessageID)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseMessageContent(response.body())
                if (parsedData.isNotEmpty()) {
                    return parsedData
                }
            }
        }catch (e: Exception){
            Log.e("MessagesRepository", "loadMessage: ${e.stackTraceToString()}")
        }
        return null}



}