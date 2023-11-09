package com.dertefter.ficus.viewmodel.timetable

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.news.NewsContent
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.TimetableRepository
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.utils.netiCore.ResponseParser
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimetableViewModel : BaseViewModel() {
    val weeksLiveData = MutableLiveData<Event<List<Week>>>()
    private val timetableRepository = TimetableRepository()

    val appPreferences = AppPreferences
    fun getWeeks(){
        weeksLiveData.postValue(Event.loading())
        if (timetableRepository.getWeeks() != null){
            val data = timetableRepository.getWeeks()
            weeksLiveData.postValue(Event.success(data))
        } else{
            viewModelScope.launch(Dispatchers.IO) {
                val data = timetableRepository.loadWeeks()
                Log.e("data", data.toString())
                if (data != null) {
                    weeksLiveData.postValue(Event.success(data))
                } else {
                    weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
                }
            }
        }

    }

    fun getTimetableForWeek(query: String){
        val weeks = timetableRepository.getWeeks()
        if (timetableRepository.getWeeks() != null){
            val data = timetableRepository.getWeeks()
            weeksLiveData.postValue(Event.success(data))
        } else{
            viewModelScope.launch(Dispatchers.IO) {
                val data = timetableRepository.loadWeeks()
                Log.e("data", data.toString())
                if (data != null) {
                    weeksLiveData.postValue(Event.success(data))
                } else {
                    weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
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