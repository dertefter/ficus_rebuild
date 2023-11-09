package com.dertefter.ficus.viewmodel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.repositoty.api.guest.GuestApi
import com.dertefter.ficus.repositoty.api.guest.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {

    var api: GuestApi = NetworkService.retrofitService()

    fun <T> requestWithLiveData(
        liveData: MutableLiveData<Event<T>>,
        parser: (ResponseBody?) -> T?,
        isNeedToReplaceData: Boolean = false,
        request: suspend () -> Response<ResponseBody>
    ) {
        val lastData = liveData.value

        liveData.postValue(Event.loading())

        this.viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = request.invoke()
                if (response.body() != null) {
                    liveData.postValue(Event.success(parser(response.body()!!)))

                } else if (response.errorBody() != null) {
                    liveData.postValue(Event.error(null))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(Event.error(null))
            }
        }
    }
}
