package com.dertefter.ficus.data

import okhttp3.ResponseBody
import org.json.JSONArray

data class Event<out T>(val status: Status, val data: JSONArray?, val error: JSONArray?) {

    companion object {
        fun <T> loading(): Event<T> {
            return Event(Status.LOADING, null, null)
        }

        fun <T> success(data: JSONArray?): Event<T> {
            return Event(Status.SUCCESS, data, null)
        }

        fun <T> error(error: JSONArray?): Event<T> {
            return Event(Status.ERROR, null, error)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}