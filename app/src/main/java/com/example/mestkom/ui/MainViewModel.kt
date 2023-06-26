package com.example.mestkom.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mestkom.data.PreferencesManager
import com.example.mestkom.data.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

    private val _isSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val isSignIn = _isSignIn
    fun isSignIn(context: Context) = viewModelScope.launch {
        delay(2500)
        val userPreferences = UserPreferences(context)
        _isSignIn.value = userPreferences.authToken.first() != null
    }
}