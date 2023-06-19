package com.example.mestkom.ui.video

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.RemoteDataSource
import com.example.mestkom.data.network.Resource
import com.example.mestkom.databinding.ActivityVideoBinding
import com.example.mestkom.databinding.ListVideoBinding
import com.example.mestkom.ui.cluster.PlacemarkUserData
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.visible
import okhttp3.ResponseBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class VideoActivity(): AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var viewModel: VideoViewModel
    private var videos = ArrayList<PlacemarkUserData>()
    private val playerItems = ArrayList<PlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videos = intent.getParcelableArrayListExtra<PlacemarkUserData>("videos") as ArrayList<PlacemarkUserData>
        viewModel = VideoViewModel(getRepository())

        adapter = VideoAdapter(this, videos,
            object : VideoAdapter.OnVideoPreparedListener {
                override fun onVideoPrepared(playerItem: PlayerItem) {
                    playerItems.add(playerItem)
                }},
            object : VideoAdapter.OnVideoLoadListener {
                override fun onLoadVideo(
                    idVideo: String,
                    listBinding: ListVideoBinding,
                    callback: (String) -> Unit
                ) {
                    viewModel.downloadResponse.observe(this@VideoActivity) { response ->
                        listBinding.animationView.visible(response is Resource.Loading)
                        when (response) {
                            is Resource.Success -> {
                                val filepath = viewModel.saveFile(response.value, applicationContext.filesDir.absolutePath + "${idVideo}.mp4")
                                Toast.makeText(applicationContext, "Got response size: ${response.value.bytes().size}. Saved at ${filepath}", Toast.LENGTH_LONG).show()
                                callback(filepath)
                            }

                            is Resource.Failure -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Failed loading video! Please, check your Internet connection and try again later",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is Resource.Loading -> {
                                listBinding.animationView.isVisible = true
                            }
                        }
                    }
                    viewModel.downloadVideo(idVideo)
                }
            }
        )

        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //Old method
                val previousIndex = playerItems.indexOfFirst { it.player.isPlaying }
                if (previousIndex != -1) {
                    val player = playerItems[previousIndex].player
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = playerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = playerItems[newIndex].player
                    player.playWhenReady = true
                    player.play()
                }

            }
        })
    }

    override fun onResume() {
        super.onResume()
        val index = binding.viewPager2.currentItem
        val playerItem = playerItems.find { it.position == index }
        if (playerItem != null) {
            val player = playerItem.player
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onPause() {
        super.onPause()
        val index = binding.viewPager2.currentItem
        val playerItem = playerItems.find { it.position == index }
        if (playerItem != null) {
            val player = playerItem.player
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (playerItems.isNotEmpty()) {
            playerItems.forEach {
                it.player.release()
            }
            playerItems.clear()
        }
    }

    private fun getRepository(): FileRepository {
        val fileApi = RemoteDataSource().buildApi(FileApi::class.java)
        return FileRepository(fileApi)
    }
}