package com.example.mestkom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mestkom.R
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.data.responses.User
import com.example.mestkom.databinding.FragmentProfileBinding
import com.example.mestkom.ui.repository.UserRepository
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.handleApiError
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.FileRepository

class ProfileFragment : BaseFragment<HomeViewModel, FragmentProfileBinding, List<BaseRepository>>() {

    var username: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner){
            when(it) {
                is Resource.Success -> updateUI(it.value)
                is Resource.Failure -> {
                    handleApiError(it)
                    logout()
                }
                else -> {
                }
            }
        }
        viewModel.getUser(requireContext())
        binding.infoButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToInfoFragment())
        }

        binding.logoutButton.setOnClickListener{
            logout()
        }
    }

    private fun updateUI(user: User) {
        binding.greeting.text = getString(R.string.welcome, user.username)
        binding.registerDate.text = getString(R.string.date, user.date.substring(0, 10))
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