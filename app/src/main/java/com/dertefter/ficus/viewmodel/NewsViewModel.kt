package com.dertefter.ficus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.repositoty.NewsRepository
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel : BaseViewModel() {
    private var page = 0
    val newsLiveData = MutableLiveData<Event<List<NewsItem>>>()
    private val newsRepository = NewsRepository()
    fun getNews(isNeedToReplaceData: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val newData = newsRepository.getNews(page, isNeedToReplaceData)
            if (newData != null) {
                newsLiveData.postValue(Event.success(newData))
            } else {
                newsLiveData.postValue(Event.error(null))
            }
        }
        page += 1
    }

    fun getNewsLiveData(): LiveData<Event<List<NewsItem>>> {
        return newsLiveData
    }
}