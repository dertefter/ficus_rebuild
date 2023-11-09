package com.dertefter.ficus.viewmodel.timetable

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.repositoty.TimetableRepository
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchGroupViewModel : BaseViewModel() {
    val liveData = MutableLiveData<Event<List<GroupItem>>>()
    private val timetableRepository = TimetableRepository()

    fun getGroups(searchQuery: String){
        liveData.postValue(Event.loading())
        viewModelScope.launch(Dispatchers.IO) {
            val data = timetableRepository.getGroups(searchQuery)
            Log.e("data", data.toString())
            if (data != null) {
                if (data.isEmpty()){
                    liveData.postValue(Event.error(Error("Ничего не нашлось... ", "")))
                }else{
                    liveData.postValue(Event.success(data))
                }

            } else {
                liveData.postValue(Event.error(Error("Не удалось загрузить данные... ", "")))
            }
        }
    }

    fun setGroup(group: String){
        timetableRepository.setCurrentGroup(group)
    }

}