package com.dertefter.ficus.repositoty

import android.util.Log
import com.dertefter.ficus.data.messages.DiSpaceMessage
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
    private var teacherChatItemList = mutableListOf<StudentStudyMessage>()
    private var otherChatItemList = mutableListOf<StudentStudyMessage>()
}