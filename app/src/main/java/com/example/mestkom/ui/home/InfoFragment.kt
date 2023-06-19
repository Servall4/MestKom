package com.example.mestkom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.navigation.fragment.findNavController
import com.example.mestkom.R
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.databinding.FragmentInfoBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.repository.UserRepository

class InfoFragment : BaseFragment<HomeViewModel, FragmentInfoBinding, List<BaseRepository>>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser(requireContext())
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_infoFragment_to_profileFragment)
        }
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = 1000

        val rotateAnimation = RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 1000
        binding.AppName.animation = alphaAnimation
        binding.Developers.animation = alphaAnimation
        binding.appIcon.animation = rotateAnimation
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentInfoBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): List<BaseRepository> {
        val api = remoteDataSource.buildApi(UserApi::class.java)
        val fileApi = remoteDataSource.buildApi(FileApi::class.java)
        return listOf(UserRepository(api), FileRepository(fileApi))
    }
}