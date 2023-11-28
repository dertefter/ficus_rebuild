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
import com.dertefter.ficus.utils.netiCore.ResponseParser

class TimetableRepository {
    private val appPreferences = AppPreferences
    private val api: GuestApi = NetworkService.retrofitService()
    private val authApi: AuthApi = AuthNetworkService.retrofitService()
    private val groupsList = mutableListOf<GroupItem>()
    private var weekList = mutableListOf<Week>()

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
        if (weekList.isNotEmpty()){
            clearWeekListData()
        }
    }

    suspend fun getCurrentGroup(): String? {
        try{
            if (AppPreferences.group == null){
                val response = authApi.getIndividualTimetable()
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseIndividualGroup(response.body())
                    if (parsedData != null) {
                        appPreferences.group = parsedData
                        return parsedData
                    }

                }
            }
            return AppPreferences.group
        } catch (e: Exception){
            Log.e("getCurrentGroup", e.stackTraceToString())
            return null
        }
    }

    suspend fun loadWeeksList(): List<Week>? {
        try {
            if (AppPreferences.group != null && AppPreferences.group == "individual"){

                val response = authApi.getIndividualTimetable()
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseWeeksForIndividual(response.body())
                    if (parsedData != null) {
                        weekList = parsedData.toMutableList()
                    }
                    return getWeeksList()
                }

            }
            else{
                val response = api.getTimetable(AppPreferences.group!!, null)
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseWeeks(response.body())
                    if (parsedData != null) {
                        weekList = parsedData.toMutableList()
                    }
                    return getWeeksList()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getWeeksList(): List<Week> {
        return weekList
    }

    suspend fun loadTimetableForWeek(weekQuery: String): Timetable? {
        try {
            if (appPreferences.group == "individual"){
                val response = authApi.getIndividualTimetable()
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseIndividualTimetable(response.body(), weekQuery)
                    if (parsedData != null) {
                        for (week in weekList){
                            if (week.query == weekQuery){
                                week.timetable = parsedData

                            }

                        }
                    }
                    return parsedData
                }
            }
            else{
                val response = api.getTimetable(AppPreferences.group!!, weekQuery)
                if (response.isSuccessful) {
                    val parsedData = ResponseParser().parseTimetable(response.body())
                    if (parsedData != null) {
                        for (week in weekList){
                            if (week.query == weekQuery){
                                week.timetable = parsedData
                            }

                        }
                    }
                    return parsedData
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getTimetableForWeek(weekQuery: String): Timetable? {
        for (week in weekList){
            if (week.query == weekQuery){
                return week.timetable
            }

        }
        return null
    }


    fun clearWeekListData() {
        weekList.clear()
    }
}