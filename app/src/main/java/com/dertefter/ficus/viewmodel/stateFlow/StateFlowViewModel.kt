package com.dertefter.ficus.viewmodel.stateFlow

import androidx.lifecycle.ViewModel
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
        if (AppPreferences.login.isNullOrEmpty() || AppPreferences.password.isNullOrEmpty()) {
            _uiState.update {
                it.copy(isAuthrized = false)
            }
        } else {
            tryAuth(AppPreferences.login!!, AppPreferences.password!!)
        }

    }

    fun updateUserData(customGroup: String? = null){
        CoroutineScope(Dispatchers.IO).launch {
            val user = authRepository.updateProfileData()
            _uiState.update {
                if (customGroup != null && customGroup != "individual"){
                    user?.customGroupTitle = customGroup
                }
                it.copy(User = user)
            }
        }
    }

    fun tryAuth(login: String, password: String){
        _uiState.update {
            it.copy(isAuthrized = null)
        }
        CoroutineScope(Dispatchers.IO).launch {
            if (authRepository.tryAuth(login, password)){
                _uiState.update {
                    it.copy(isAuthrized = true)
                }
            } else {
                _uiState.update {
                    it.copy(isAuthrized = false)
                }
            }

        }

    }


}