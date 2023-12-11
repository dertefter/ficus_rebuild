package com.dertefter.ficus.data

import android.hardware.SensorAdditionalInfo
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Week

data class Event<out T>(val status: Status, val data: T?, val error: Error?, val additionalInfo: String? = null) {

    companion object {
        fun <T> loading(): Event<T> {
            return Event(Status.LOADING, null, null)
        }

        fun <T> success(data: T?, additionalInfo: String? = null): Event<T & Any> {
            return Event(Status.SUCCESS, data, null, additionalInfo)
        }

        fun <T> error(error: Error?): Event<T> {
            return Event(Status.ERROR, null, error)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}