package com.example.mestkom.ui.auth

import android.util.Patterns
import androidx.lifecycle.*
import com.example.mestkom.data.network.Resource
import com.example.mestkom.ui.repository.AuthRepository
import com.example.mestkom.data.responses.AuthResponse
import com.example.mestkom.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<AuthResponse>> = _loginResponse

    private val _registerResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val registerResponse: LiveData<Resource<AuthResponse>> = _registerResponse

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

    fun validateLoginInput(
        username: String,
        password: String
    ): List<Boolean> {
        val list = mutableListOf<Boolean>()
        list.add(password.length >= 8)
        list.add(username.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$")))
        return list
    }

    fun validateRegisterInput(
        username: String,
        password: String,
        passwordCorrect: String,
        email: String
    ): List<Boolean> {
        val list = mutableListOf<Boolean>()
        list.add(username.matches(Regex("^(?=.*[A-Za-z0-9]\$)[A-Za-z][A-Za-z\\d.-]{0,19}\$")))
        list.add(password.length >= 8)
        list.add(passwordCorrect == password)
        list.add(Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return list
    }

    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }

    suspend fun saveUserId(id: String) {
        repository.saveUserId(id)
    }
}