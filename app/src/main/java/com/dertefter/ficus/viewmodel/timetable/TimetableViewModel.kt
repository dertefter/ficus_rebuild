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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimetableViewModel : BaseViewModel() {
    val weeksLiveData = MutableLiveData<Event<List<Week>>>()
    val groupLiveData = MutableLiveData<Event<String>>()
    private var timetableRepository = TimetableRepository()
    private val appPreferences = AppPreferences

    fun clearData(){
        timetableRepository = TimetableRepository()
    }
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
        viewModelScope.launch(Dispatchers.IO) {
            if (timetableRepository.getTimetableForWeek(weekQuery) != null && !timetableRepository.getTimetableForWeek(weekQuery)!!.days.isNullOrEmpty()){
                val data = timetableRepository.getWeeksList()
                withContext(Dispatchers.Main){
                    weeksLiveData.postValue(Event.success(data))
                }
            } else{
                withContext(Dispatchers.Main){
                    weeksLiveData.postValue(Event.loading())
                }
                val data = timetableRepository.loadTimetableForWeek(weekQuery)
                if (data != null) {
                    withContext(Dispatchers.Main){
                        weeksLiveData.postValue(Event.success(timetableRepository.getWeeksList()))
                    }
                } else {
                    weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
                }
            }
        }

    }



    fun getGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            groupLiveData.postValue(Event.success(timetableRepository.getCurrentGroup()))
        }
    }

    fun setGroup(group: String) {
        appPreferences.group = group
    }
}