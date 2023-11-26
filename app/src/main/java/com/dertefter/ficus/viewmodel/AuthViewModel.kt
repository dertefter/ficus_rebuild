package com.dertefter.ficus.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dertefter.ficus.data.Event
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.repositoty.TimetableRepository
import com.dertefter.ficus.repositoty.api.student_study.AuthNetworkService
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.dertefter.ficus.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : BaseViewModel() {

    private val appPreferences = AppPreferences


}