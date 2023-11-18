package com.dertefter.ficus.repositoty

import android.util.Log
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.utils.netiCore.ResponseParser

class TimetableRepository {
    private val appPreferences = AppPreferences
    private val api: GuestApi = NetworkService.retrofitService()
    private val groupsList = mutableListOf<GroupItem>()
    private var weekList = mutableListOf<Week>()
    private var timetableMap = mutableMapOf<String, Timetable?>()

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

    suspend fun getCurrentGroup(): String? {
        return AppPreferences.group
    }

    suspend fun loadWeeksList(): List<Week>? {
        try {
            val response = api.getTimetable(AppPreferences.group!!, null)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseWeeks(response.body())
                if (parsedData != null) {
                    weekList = parsedData.toMutableList()
                }
                return getWeeksList()
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
            val response = api.getTimetable(AppPreferences.group!!, weekQuery)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseTimetable(response.body())
                timetableMap[weekQuery] = parsedData
                return timetableMap.get(weekQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getTimetableForWeek(weekQuery: String): Timetable? {
        return timetableMap[weekQuery]
    }

    fun getTimeTableMap(): Map<String, Timetable?> {
        return timetableMap
    }
}