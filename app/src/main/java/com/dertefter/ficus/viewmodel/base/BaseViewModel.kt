package com.dertefter.ficus.viewmodel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.repositoty.api.ResponseWrapper
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import com.dertefter.ficus.repositoty.api.student_study.Api
import com.dertefter.ficus.utils.netiCore.ResponseParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {

    var api: GuestApi = NetworkService.retrofitService()

    // У нас будут две базовые функции requestWithLiveData и 
    // requestWithCallback, в зависимости от ситуации мы будем
    // передавать в них лайвдату или колбек вместе с параметрами сетевого
    // запроса. Функция принимает в виде параметра ретрофитовский suspend запрос, 
    // проверяет на наличие ошибок и сетит данные в виде ивента либо в 
    // лайвдату либо в колбек. Про ивент будет написано ниже

    fun <T> requestWithLiveData(
        liveData: MutableLiveData<Event<T>>,
        request: suspend () -> Response<ResponseBody>
    ) {

        // В начале запроса сразу отправляем ивент загрузки
        liveData.postValue(Event.loading())

        // Привязываемся к жизненному циклу ViewModel, используя viewModelScope.
        // После ее уничтожения все выполняющиеся длинные запросы 
        // будут остановлены за ненадобностью.
        // Переходим в IO поток и стартуем запрос
        this.viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.invoke()
                if (response.body() != null) {
                    val NewsList = ResponseParser().parseNews(response.body())
                    liveData.postValue(Event.success(NewsList))
                } else if (response.errorBody() != null) {
                    liveData.postValue(Event.error(null))
                }else{
                    liveData.postValue(Event.error(null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Event.error(null))
            }
        }
    }
}
