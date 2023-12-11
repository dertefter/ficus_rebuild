package com.dertefter.ficus.viewmodel.stateFlow

import androidx.lifecycle.ViewModel
import com.dertefter.ficus.data.TimetableData
import com.dertefter.ficus.data.UiStateData
import com.dertefter.ficus.repositoty.AuthRepository
import com.dertefter.ficus.repositoty.local.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StateFlowViewModel: ViewModel()   {
    private val _uiState = MutableStateFlow(UiStateData())
    val uiState = _uiState
    var appPreferences = AppPreferences
    val authRepository = AuthRepository()

    fun checkAuth() {
        if (appPreferences.login.isNullOrEmpty() || appPreferences.password.isNullOrEmpty()) {
            _uiState.update {
                it.copy(isAuthrized = false)
            }
        } else {
            tryAuth(AppPreferences.login!!, AppPreferences.password!!)
        }
    }

    fun updateUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            val user = authRepository.updateProfileData()
            _uiState.update {
                it.copy(User = user).copy(timetableData = TimetableData(group = "individual", groupTitle = user?.groupTitle))
            }
        }
    }

    fun updateTimetableData(){
        if (_uiState.value.isAuthrized == true){
            updateUserData()
        }else{
            _uiState.update {
                it.copy(timetableData = TimetableData(group = appPreferences.group, groupTitle = appPreferences.group))
            }
        }
    }

    fun tryAuth(login: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            if (authRepository.tryAuth(login, password)){
                _uiState.update {
                    it.copy(isAuthrized = true, authError = false)
                }
            } else {
                _uiState.update {
                    it.copy(isAuthrized = true, authError = true)
                }
            }

        }

    }

    fun setGroup(group: String, individualGroupTitle: String? = null) {
        appPreferences.group = group
        if (group == "individual"){
            _uiState.update {
                it.copy(timetableData = it.timetableData?.copy(group = group)?.copy(groupTitle = individualGroupTitle))
            }
        }else{
            _uiState.update {
                it.copy(timetableData = it.timetableData?.copy(group = group)?.copy(groupTitle = group))
            }
        }
    }




}