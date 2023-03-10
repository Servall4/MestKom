package com.example.mestkom.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.repository.UserRepository
import com.example.mestkom.data.responses.LoginResponse
import com.example.mestkom.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository
): BaseViewModel(repository) {

    private val _user: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val user: LiveData<Resource<LoginResponse>>
    get() = _user

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        _user.value = repository.getUser()
    }
}