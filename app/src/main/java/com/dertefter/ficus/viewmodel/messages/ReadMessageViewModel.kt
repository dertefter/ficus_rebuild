package com.dertefter.ficus.viewmodel.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.data.messages.StudentStudyMessage
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.repositoty.MessagesRepository
import com.dertefter.ficus.repositoty.NewsRepository
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReadMessageViewModel : BaseViewModel() {
    val liveData = MutableLiveData<Event<String>>()
    private val messagesRepository = MessagesRepository()
    fun loadMessage(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = messagesRepository.loadMessage(id)
            if (!data.isNullOrEmpty()){
                liveData.postValue(Event.success(data))
            }else{
                liveData.postValue(Event.error(null))
            }
        }
    }

}