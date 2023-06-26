package com.example.mestkom.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.mestkom.R
import com.example.mestkom.data.PreferencesManager
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.databinding.FragmentHomeBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.cluster.ClusterView
import com.example.mestkom.ui.cluster.PlacemarkUserData
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.repository.UserRepository
import com.example.mestkom.ui.video.VideoActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, List<BaseRepository>>() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val placemarkTapListener = MapObjectTapListener { mapObject, _ ->
        val placemark = mapObject as PlacemarkMapObject
        val intent = Intent(context, VideoActivity::class.java)
        val userData = placemark.userData as PlacemarkUserData
        val video = arrayListOf(userData)
        intent.putParcelableArrayListExtra("videos", video)
        getUsername {
            intent.putExtra("username", it)
        }
        startActivity(intent)
        true
    }

    private lateinit var clasterizedCollection: ClusterizedPlacemarkCollection

    private val clusterListener = ClusterListener { cluster ->
        cluster.appearance.setView(
            ViewProvider(
                ClusterView(requireContext()).apply {
                    updateViews(cluster.size)
                }
            )
        )
        cluster.appearance.zIndex = 100f
        cluster.addClusterTapListener(clusterTapListener)
    }

    private val clusterTapListener = ClusterTapListener {
        binding.mapview.map.move(
            CameraPosition(Point(it.appearance.geometry.latitude, it.appearance.geometry.longitude), 4.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1.5F), null
        )
        val videos = ArrayList<PlacemarkUserData>()
        it.placemarks.forEach { placemark ->
            val userData = placemark.userData as PlacemarkUserData
            videos.add(userData)
        }
        val intent = Intent(context, VideoActivity::class.java)
        intent.putParcelableArrayListExtra("videos", videos)
        getUsername {
            intent.putExtra("username", it)
        }
        startActivity(intent)
        true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.mapview.map.isNightModeEnabled = false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.mapview.map.isNightModeEnabled = true
            }
        }
        viewModel.user.observe(viewLifecycleOwner){
            when(it) {
                is Resource.Failure -> {
                    Toast.makeText(context, "Your session was expired! Please sign in again.", Toast.LENGTH_SHORT).show()
                    logout()
                }
                else -> {
                }
            }
        }

        viewModel.getUser(requireContext())

        viewModel.updateResponse.observe(viewLifecycleOwner) {
            clasterizedCollection = binding.mapview.map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)
            when(it) {
                is Resource.Success -> {
                    it.value.forEach {
                        clasterizedCollection.addPlacemark(Point(it.latitude.toDouble(), it.longitude.toDouble()), ImageProvider.fromBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.video_icon)!!.toBitmap())).apply {
                            addTapListener(placemarkTapListener)
                            setText(it.nameVideo, TextStyle().apply {
                                size = 10f
                                placement = TextStyle.Placement.BOTTOM
                                offset = 5f
                                userData = PlacemarkUserData(it.nameVideo, it.description, it.idVideo)
                            })
                        }
                    }
                    clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
                }
                is Resource.Failure -> {
                    Toast.makeText(context, "Can't upload videos, try again later", Toast.LENGTH_LONG).show()
                }
                else -> {

                }
            }
        }

        viewModel.updateVideos()
        viewModel.observeLocation(viewLifecycleOwner) {
            binding.mapview.map.move(
                CameraPosition(Point(it.first, it.second), 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1.5F), null
            )
        }

        viewModel.getLocation(PreferencesManager.Base(requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)))

        if (!Build.HARDWARE.equals("ranchu")) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            if (savedInstanceState == null) {
                getLocation()
            }
        }

        binding.findMeButton.setOnClickListener {
            getLocation()
            viewModel.getLocation(PreferencesManager.Base(requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)))
        }
    }

    private fun getUsername(callback: (String) -> Unit) {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it is Resource.Success)
                callback(it.value.username)
        }
        viewModel.getUser(requireContext())
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }
    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

     override fun getFragmentRepository(): List<BaseRepository> {
        val api = remoteDataSource.buildApi(UserApi::class.java)
         val fileApi = remoteDataSource.buildApi(FileApi::class.java)
        return listOf(UserRepository(api), FileRepository(fileApi))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.mapview.map.isNightModeEnabled = false
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.mapview.map.isNightModeEnabled = true
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) return true

        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "You have the Permission")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Log.d("TAG", "LOCATION IS NULL")
                    } else {
                        val lat = location.latitude
                        val long = location.longitude
                        viewModel.saveLocation(lat, long, PreferencesManager.Base(requireContext().getSharedPreferences("pref",Context.MODE_PRIVATE)))
                    }
                }
            } else {
                Toast.makeText(context, "Please Turn on Your device Location", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            requestPermissions()
        }
    }

    private  companion object {
        const val PERMISSION_ID = 100
    }
}