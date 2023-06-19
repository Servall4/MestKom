package com.example.mestkom.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mestkom.ui.repository.AuthRepository
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.UserRepository
import com.example.mestkom.ui.auth.AuthViewModel
import com.example.mestkom.ui.home.HomeViewModel
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.video.VideoViewModel
import kotlin.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repositories: List<BaseRepository>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repositories.first() as AuthRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repositories.first() as UserRepository, repositories[1] as FileRepository) as T
            modelClass.isAssignableFrom(VideoViewModel::class.java) -> VideoViewModel(repositories.first() as FileRepository) as T
            else -> throw IllegalArgumentException("View Model class not found")
        }
    }

}