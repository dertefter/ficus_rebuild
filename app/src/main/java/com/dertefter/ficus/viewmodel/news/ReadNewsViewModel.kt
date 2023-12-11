package com.dertefter.ficus.viewmodel.news

import androidx.lifecycle.MutableLiveData
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.news.NewsContent
import com.dertefter.ficus.utils.netiCore.ResponseParser
import com.dertefter.ficus.viewmodel.base.BaseViewModel

class ReadNewsViewModel : BaseViewModel() {
    val newsLiveData = MutableLiveData<Event<NewsContent>>()
    fun getNewsMore(idnews: String?) {
        requestWithLiveData(newsLiveData, isNeedToReplaceData = true, parser = { ResponseParser().parseNewsMore(it) }) {
            api.getNewsMore(idnews)
        }
    }
}