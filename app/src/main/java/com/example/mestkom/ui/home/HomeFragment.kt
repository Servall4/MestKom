package com.example.mestkom.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.mestkom.R
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.network.UserApi
import com.example.mestkom.databinding.FragmentHomeBinding
import com.example.mestkom.ui.base.BaseFragment
import com.example.mestkom.ui.cluster.ClusterView
import com.example.mestkom.ui.cluster.PlacemarkUserData
import com.example.mestkom.ui.hasPermission
import com.example.mestkom.ui.repository.BaseRepository
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.repository.UserRepository
import com.example.mestkom.ui.requestPermissionWithRationale
import com.example.mestkom.ui.video.VideoActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider


private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, List<BaseRepository>>(),
    UserLocationObjectListener {

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

    private lateinit var userLocationLayer: UserLocationLayer

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
            CameraPosition(
                Point(it.appearance.geometry.latitude, it.appearance.geometry.longitude),
                4.0f,
                0.0f,
                0.0f
            ),
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestFineLocationPermission()
            requestBackgroundLocationPermission()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLocationLayer =
            MapKitFactory.getInstance().createUserLocationLayer(binding.mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.setObjectListener(this)

        viewModel.initRepository(requireContext())
        when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.mapview.map.isNightModeEnabled = false
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                binding.mapview.map.isNightModeEnabled = true
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            if (it is Resource.Failure) {
                Toast.makeText(
                    context,
                    "Your session was expired! Please sign in again.",
                    Toast.LENGTH_SHORT
                ).show()
                logout()
            }
        }

        viewModel.getUser(requireContext())

        viewModel.updateResponse.observe(viewLifecycleOwner) {
            clasterizedCollection =
                binding.mapview.map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)
            when (it) {
                is Resource.Success -> {
                    it.value.forEach {
                        clasterizedCollection.addPlacemark(
                            Point(
                                it.latitude.toDouble(),
                                it.longitude.toDouble()
                            ),
                            ImageProvider.fromBitmap(
                                AppCompatResources.getDrawable(
                                    requireContext(),
                                    R.drawable.video_icon
                                )!!.toBitmap()
                            )
                        ).apply {
                            addTapListener(placemarkTapListener)
                            setText(it.nameVideo, TextStyle().apply {
                                size = 10f
                                placement = TextStyle.Placement.BOTTOM
                                offset = 5f
                                userData =
                                    PlacemarkUserData(it.nameVideo, it.description, it.idVideo)
                            })
                        }
                    }
                    clasterizedCollection.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
                }

                is Resource.Failure -> {
                    Toast.makeText(context, "Can't get videos, try again later", Toast.LENGTH_LONG)
                        .show()
                }

                else -> {

                }
            }
        }

        viewModel.updateVideos()

        viewModel.getLocation().observe(viewLifecycleOwner) { location ->
            binding.mapview.map.move(
                CameraPosition(
                    Point(location.first.toDouble(), location.second.toDouble()),
                    15.0f,
                    0.0f,
                    0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1.5F), null
            )
        }

        binding.findMeButton.setOnClickListener {
            val position = viewModel.getLocation().value!!
            binding.mapview.map.move(
                CameraPosition(
                    Point(
                        position.first.toDouble(),
                        position.second.toDouble()
                    ),
                    15.0f,
                    0.0f,
                    0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1.5F), null
            )
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
        viewModel.stopLocationUpdates()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLocationUpdates()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d("PermissionResult", "onRequestPermissionResult")
        when (requestCode) {
            REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE,
            REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive an empty array.
                    Log.d("HomeFragment", "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    Toast.makeText(context, "You gave permission", Toast.LENGTH_LONG).show()

                else -> {

                    val permissionDeniedExplanation =
                        if (requestCode == REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {
                            Toast.makeText(
                                context,
                                "You should gave fine location permission to properly work application",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "You should gave background location permission to properly work application",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
    }

    override fun onObjectAdded(p0: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF(
                (binding.mapview.width() * 0.5).toFloat(),
                (binding.mapview.height() * 0.5).toFloat()
            ),
            PointF(
                (binding.mapview.width() * 0.5).toFloat(),
                (binding.mapview.height() * 0.83).toFloat()
            )
        )

        p0.arrow.setIcon(ImageProvider.fromResource(context, R.drawable.cluster_view_background))

        val pinIcon: CompositeIcon = p0.pin.useCompositeIcon()

//        p0.accuracyCircle.fillColor(Color.BLUE and -0x66000001)
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    private fun requestFineLocationPermission() {
        val permissionApproved =
            context?.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ?: return

        if (!permissionApproved) {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun requestBackgroundLocationPermission() {
        val permissionApproved =
            context?.hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) ?: return

        if (!permissionApproved) {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val ARG_PERMISSION_REQUEST_TYPE =
            "com.example.mestkom.PERMISSION_REQUEST_TYPE"
        private const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 56
    }

}