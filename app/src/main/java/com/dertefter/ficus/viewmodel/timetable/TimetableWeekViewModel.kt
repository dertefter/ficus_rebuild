package com.dertefter.ficus.viewmodel.timetable

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.data.timetable.WeekDao
import com.dertefter.ficus.repositoty.TimetableRepository
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.repositoty.local.room.AppDatabase
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimetableWeekViewModel : BaseViewModel() {
    val weeksLiveData = MutableLiveData<Event<List<Week>>>()
    val groupLiveData = MutableLiveData<Event<String>>()
    private var timetableRepository = TimetableRepository()
    private val appPreferences = AppPreferences
    var weekDao: WeekDao? = null

    fun initWeekDao(application: Application){
        weekDao = AppDatabase.getAppDataBase(application)?.weekDao()
    }

    fun getTimetableForWeek(weekQuery: String){
        viewModelScope.launch(Dispatchers.IO) {
            weeksLiveData.postValue(Event.loading())
            val data = timetableRepository.loadTimetableForWeek(weekQuery)
            if (data != null) {
                val week = weekDao?.getWeek(weekQuery)
                week?.timetable = data
                weekDao?.insertWeeks(listOf(week!!))
                weeksLiveData.postValue(Event.success(weekDao?.getWeeks()))
            } else if (!weekDao?.getWeek(weekQuery)?.timetable?.days.isNullOrEmpty()){
            weeksLiveData.postValue(Event.success(weekDao?.getWeeks()))
            }
            else{
                weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ")))
            }
        }
    }
}