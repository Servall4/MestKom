package com.example.mestkom.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mestkom.data.repository.AuthRepository
import com.example.mestkom.data.repository.BaseRepository
import com.example.mestkom.data.repository.UserRepository
import com.example.mestkom.ui.auth.AuthViewModel
import com.example.mestkom.ui.home.HomeViewModel
import kotlin.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as UserRepository) as T
            else -> throw IllegalArgumentException("View Model class not found")
        }
    }

}