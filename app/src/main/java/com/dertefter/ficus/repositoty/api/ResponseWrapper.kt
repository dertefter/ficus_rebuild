package com.dertefter.ficus.repositoty.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseWrapper<T> : Serializable {
    @SerializedName("response")
    val response: T? = null
    @SerializedName("error")
    val error: Error? = null
}