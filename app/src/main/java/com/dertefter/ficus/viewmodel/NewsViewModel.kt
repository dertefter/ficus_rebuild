package com.dertefter.ficus.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import org.json.JSONArray

class NewsViewModel : BaseViewModel() {

    // Создаем лайвдату для нашего списка юзеров
    val newsLiveData = MutableLiveData<Event<JSONArray>>()

    // Получение юзеров. Обращаемся к функции  requestWithLiveData
    // из BaseViewModel передаем нашу лайвдату и говорим,
    // какой сетевой запрос нужно выполнить и с какими параметрами
    // В данном случае это api.getUsers
    // Теперь функция сама выполнит запрос и засетит нужные
    // данные в лайвдату
    fun getNews(page: Int? = 1) {
        requestWithLiveData(newsLiveData) {
            api.getNews(
                page = page.toString()
            )
        }
    }
}