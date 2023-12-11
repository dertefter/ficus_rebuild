package com.dertefter.ficus.data.messages

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentStudyMessage(
    val mes_id: String,
    val title: String,
    val text: String,
    var is_new: Boolean = false
): Parcelable
