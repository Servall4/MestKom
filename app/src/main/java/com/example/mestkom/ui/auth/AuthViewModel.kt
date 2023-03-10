package com.example.mestkom.ui.auth

import android.app.usage.UsageEvents
import android.media.metrics.Event
import android.util.EventLog
import androidx.lifecycle.*
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.repository.AuthRepository
import com.example.mestkom.data.responses.LoginResponse
import com.example.mestkom.data.responses.RegisterResponse
import com.example.mestkom.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
): BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _registerResponse: MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()
    val registerResponse: LiveData<Resource<RegisterResponse>>
        get() = _registerResponse

    fun login(
        username: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(username, password)
    }

    fun register(
        username: String,
        password: String,
        email: String
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.register(username, password, email)
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }
}