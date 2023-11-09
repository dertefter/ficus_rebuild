package com.dertefter.ficus.repositoty

import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.utils.netiCore.ResponseParser

class TimetableRepository {
    private val api: GuestApi = NetworkService.retrofitService()
    private val groupsData = mutableListOf<GroupItem>()
    private var weeksList: List<Week>? = null
    private val appPreferences = AppPreferences
    suspend fun getGroups(searchQuery: String): List<GroupItem>? {
        try {
            val response = api.getGroupList(searchQuery)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseGroups(response.body())
                if (parsedData != null) {
                    groupsData.clear()
                    groupsData.addAll(parsedData)
                }
                return groupsData
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun loadWeeks(): List<Week>? {
        try {
            val response = api.getTimetable(AppPreferences.group!!, null)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseWeeks(response.body())
                if (parsedData != null) {
                    weeksList = parsedData
                }
                return weeksList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun loadTimetableForWeek(week: String): List<Week>? {
        try {
            val response = api.getTimetable(AppPreferences.group!!, week)
            if (response.isSuccessful) {
                val parsedData = ResponseParser().parseWeeks(response.body())
                if (parsedData != null) {
                    weeksList = parsedData
                }
                return weeksList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getWeeks(): List<Week>?{
        return weeksList
    }

     fun setCurrentGroup(groupTitle: String){
        appPreferences.group = groupTitle
    }

    suspend fun getCurrentGroup(): String? {
        return AppPreferences.group
    }
}