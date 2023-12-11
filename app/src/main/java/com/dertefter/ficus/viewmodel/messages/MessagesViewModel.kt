package com.dertefter.ficus.viewmodel.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.repositoty.MessagesRepository
import com.dertefter.ficus.repositoty.NewsRepository
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessagesViewModel : BaseViewModel() {
    val teacherMessagesLiveData = MutableLiveData<Event<List<StudentStudyChatItem>>>()
    val otherMessagesLiveData = MutableLiveData<Event<List<StudentStudyChatItem>>>()
    private val messagesRepository = MessagesRepository()
    fun getTeacherMessages(forceUpdate: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = messagesRepository.getTeacherChatList()
            if (data.isNotEmpty() && !forceUpdate) {
                teacherMessagesLiveData.postValue(Event.success(data))
            } else {
                teacherMessagesLiveData.postValue(Event.success(messagesRepository.updateTeacherChatList()))
            }
        }
    }

    fun getOtherMessages(forceUpdate: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = messagesRepository.getOtherChatList()
            if (data.isNotEmpty() && !forceUpdate) {
                otherMessagesLiveData.postValue(Event.success(data))
            } else {
                otherMessagesLiveData.postValue(Event.success(messagesRepository.updateOtherChatList()))
            }
        }
    }

}