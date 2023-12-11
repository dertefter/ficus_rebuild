package com.dertefter.ficus.data.messages

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentStudyChatItem(
    val send_by: String,
    var is_new: Boolean = false,
    var messages: List<StudentStudyMessage> = listOf<StudentStudyMessage>()
): Parcelable
