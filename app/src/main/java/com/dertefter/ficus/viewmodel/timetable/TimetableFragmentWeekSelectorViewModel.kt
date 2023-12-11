package com.dertefter.ficus.viewmodel.timetable

import android.app.Application
import android.util.Log
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

class TimetableFragmentWeekSelectorViewModel : BaseViewModel() {
    val weeksLiveData = MutableLiveData<Event<List<Week>>>()
    private var timetableRepository = TimetableRepository()
    private val appPreferences = AppPreferences
    var weekDao: WeekDao? = null

    fun initWeekDao(application: Application){
        weekDao = AppDatabase.getAppDataBase(application)?.weekDao()
    }

    fun clearData(){
        timetableRepository = TimetableRepository()
    }
    fun getWeekList(individual: Boolean = false){
        CoroutineScope(Dispatchers.IO).launch {
            weeksLiveData.postValue(Event.loading())
            val data = timetableRepository.loadWeeksList()
            if (data != null) {
                weekDao?.insertWeeks(data)
                weeksLiveData.postValue(Event.success(data))
            }else if (!weekDao?.getWeeks().isNullOrEmpty()){
                weeksLiveData.postValue(Event.success(weekDao?.getWeeks()))
            }else{
                weeksLiveData.postValue(Event.error(Error("Не удалось загрузить данные... ")))

            }
        }
    }

}