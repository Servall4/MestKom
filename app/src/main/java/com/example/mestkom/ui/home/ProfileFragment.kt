package com.example.mestkom.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mestkom.R
import com.example.mestkom.camera.CameraActivity
import com.example.mestkom.data.PreferencesManager
import com.example.mestkom.data.UserPreferences
import com.example.mestkom.data.map.LocationCommunication
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.RemoteDataSource
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.data.responses.AuthResponse
import com.example.mestkom.data.responses.User
import com.example.mestkom.databinding.FragmentProfileBinding
import com.example.mestkom.ui.repository.UserRepository
import com.example.mestkom.ui.auth.AuthActivity
import com.example.mestkom.ui.auth.LoginFragmentDirections
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.startNewActivity
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileFragment : BaseFragment<HomeViewModel, FragmentProfileBinding, List<BaseRepository>>() {

    var username: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.place.map.isNightModeEnabled = false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.place.map.isNightModeEnabled = true
            }
        }
//        viewModel.user.observe(viewLifecycleOwner){
//            when(it) {
//                is Resource.Success -> updateUI(it.value)
//                is Resource.Failure -> {
//                    handleApiError(it)
//                    logout()
//                }
//                else -> {
//                }
//            }
//        }
//        viewModel.getUser(requireContext())
        binding.infoButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToInfoFragment())
        }

        binding.logoutButton.setOnClickListener{
            logout()
        }
    }

    private fun updateUI(user: User) {
        binding.greeting.text = getString(R.string.welcome, user.username)
        binding.registerDate.text = getString(R.string.date, user.date)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.place.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): List<BaseRepository> {
        val api = remoteDataSource.buildApi(UserApi::class.java)
        val fileApi = remoteDataSource.buildApi(FileApi::class.java)
        return listOf(UserRepository(api), FileRepository(fileApi))
    }
}