package com.example.mestkom.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.data.repository.UserRepository
import com.example.mestkom.data.responses.User
import com.example.mestkom.databinding.FragmentHomeBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.base.BaseScreen
import com.example.mestkom.ui.visible
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeFragment() : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {

    lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = binding.mapview
        MapKitFactory.initialize(activity)
        mapView.map.move(
            CameraPosition(Point(64.538371, 40.512726), 0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 300f), null)
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onViewCreated(view, savedInstanceState)
        /*
        binding.progressBar2.visible(false)


        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar2.visible(false)
                    updateUi(it.value.user)
                }

                is Resource.Loading -> {
                    binding.progressBar2.visible(true)
                }

                is Resource.Failure -> {
                    null
                }
            }
        })

        binding.logout.setOnClickListener{
            logout()
        }
        */
    }
    private fun updateUi(user: User) {
        with(binding) {
           /* textViewUsername.text = user.username
            textViewId.text = user.id.toString()
            textViewEmail.text = user.email*/

        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }

}