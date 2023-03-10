package com.example.mestkom.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel (
    private val repository: BaseRepository
): ViewModel() {

    suspend fun logout(api: UserApi) = withContext(Dispatchers.IO) { repository.logout(api) }
}