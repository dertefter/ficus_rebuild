package com.dertefter.ficus.repositoty

import android.util.Log
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.repositoty.api.student_study.AuthApi
import com.dertefter.ficus.repositoty.api.student_study.AuthNetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.repositoty.local.room.AppDatabase
import com.dertefter.ficus.utils.netiCore.ResponseParser

class TimetableRepository {
    private val appPreferences = AppPreferences
    private val api: GuestApi = NetworkService.retrofitService()
    private val authApi: AuthApi = AuthNetworkService.retrofitService()
    private val groupsList = mutableListOf<GroupItem>()


    suspend fun loadGroups(searchQuery: String): List<GroupItem>? {
        try {
            val response = api.getGroupList(searchQuery)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseGroups(response.body())
                if (parsedData != null) {
                    groupsList.clear()
                    groupsList.addAll(parsedData)
                }
                return groupsList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun setCurrentGroup(groupTitle: String){
        appPreferences.group = groupTitle
    }

    suspend fun loadWeeksList(): List<Week>? {
        try {
            if (appPreferences.group == "individual"){
                Log.e("loadWeeksList", "individual")
                val response = authApi.getIndividualTimetable()
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseWeeksForIndividual(response.body())
                    if (parsedData != null) {
                        Log.e("loadWeeksList", "$parsedData")
                        return parsedData.toMutableList()
                    }
                }
            }
            else{
                Log.e("loadWeeksList", "not individual, group: ${AppPreferences.group}")
                val response = api.getTimetable(AppPreferences.group!!, null)
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseWeeks(response.body())
                    if (parsedData != null) {
                        return parsedData.toMutableList()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun loadTimetableForWeek(weekQuery: String): Timetable? {
        try {
            Log.e("loadTimetableForWeek", "group: ${AppPreferences.group}")
            if (appPreferences.group == "individual"){
                val response = authApi.getIndividualTimetable()
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseIndividualTimetable(response.body(), weekQuery)
                    if (parsedData != null) {
                        val timetable = parsedData
                        timetable.group = "individual"
                        return timetable
                    }
                }
            }
            else{
                val response = api.getTimetable(AppPreferences.group!!, weekQuery)
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseTimetable(response.body())
                    if (parsedData != null) {
                        parsedData.group = AppPreferences.group!!
                        return parsedData
                    }
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}