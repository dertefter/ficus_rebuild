package com.dertefter.ficus.viewmodel.timetable

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.TimetableRepository
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimetableViewModel : BaseViewModel() {
    val weeksLiveData = MutableLiveData<Event<List<Week>>>()
    val timetableLiveData = MutableLiveData<Event<Map<String, Timetable?>>>()
    private val timetableRepository = TimetableRepository()
    init {
        timetableRepository.getWeeksList()
    }
    val appPreferences = AppPreferences
    fun getWeekList(){
        if (timetableRepository.getWeeksList().isNotEmpty()){
            val data = timetableRepository.getWeeksList()
            weeksLiveData.postValue(Event.success(data))
        } else{
            viewModelScope.launch(Dispatchers.IO) {
                val data = timetableRepository.loadWeeksList()
                if (data != null) {
                    withContext(Dispatchers.Main){
                        weeksLiveData.postValue(Event.success(data))
                    }
                } else {
                    weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
                }
            }
        }
    }

    fun getTimetableForWeek(weekQuery: String){
        if (timetableRepository.getTimetableForWeek(weekQuery) != null){
            viewModelScope.launch(Dispatchers.IO) {
                val data = timetableRepository.getTimeTableMap()
                withContext(Dispatchers.Main){
                    timetableLiveData.postValue(Event.success(data))
                }
            }
        } else{
            viewModelScope.launch(Dispatchers.IO) {
                Log.e("eeesaved not", timetableRepository.getTimeTableMap().toString())
                val data = timetableRepository.loadTimetableForWeek(weekQuery)
                if (data != null) {
                    withContext(Dispatchers.Main){
                        Log.e("eeesaved not", timetableRepository.getTimeTableMap().toString())
                        timetableLiveData.postValue(Event.success(timetableRepository.getTimeTableMap()))
                    }
                } else {
                    timetableLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
                }
            }
        }

    }
    fun getGroup(): String? {
        return appPreferences.group
    }

    fun setGroup(group: String) {
        appPreferences.group = group
    }
}