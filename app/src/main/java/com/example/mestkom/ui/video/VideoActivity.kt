package com.example.mestkom.ui.video

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.example.mestkom.data.network.FileApi
import com.example.mestkom.data.network.RemoteDataSource
import com.example.mestkom.data.network.Resource
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.databinding.ActivityVideoBinding
import com.example.mestkom.databinding.ListVideoBinding
import com.example.mestkom.ui.cluster.PlacemarkUserData
import com.example.mestkom.ui.repository.FileRepository
import com.example.mestkom.ui.visible
import java.io.File


class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var adapter: VideoAdapter
    private lateinit var viewModel: VideoViewModel
    private var videos = ArrayList<PlacemarkUserData>()
    private val playerItems = ArrayList<PlayerItem>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videos =
            intent.getParcelableArrayListExtra<PlacemarkUserData>("videos") as ArrayList<PlacemarkUserData>
        username = intent.getStringExtra("username")
        viewModel = VideoViewModel(getRepository())

        adapter = VideoAdapter(
            this, videos,
            object : VideoAdapter.OnVideoPreparedListener {
                override fun onVideoPrepared(playerItem: PlayerItem) {
                    playerItems.add(playerItem)
                }
            },
            object : VideoAdapter.OnVideoLoadListener {

                override fun onLoadComment(
                    idVideo: String,
                    callback: (List<CommentResponse>) -> Unit
                ) {
                    val commentsResponse = viewModel.getComments(idVideo)
                    commentsResponse.observe(this@VideoActivity) { response ->
                        if (response is Resource.Success) {
                            callback(response.value)

                        }

                        if (response is Resource.Failure) {
                            Toast.makeText(
                                applicationContext,
                                "Can't get comments for this video! Please, try again later",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            },
            object : VideoAdapter.OnOpenCommentListener {
                override fun onOpenComment(comments: List<CommentResponse>, idVideo: String) {
                    val actionCommentFragment =
                        ActionCommentFragment.newInstance(comments, viewModel, idVideo, username!!)
                    actionCommentFragment.show(supportFragmentManager, actionCommentFragment.tag)
                }
            },
            viewModel
        )

        binding.viewPager2.adapter = adapter
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val previousIndex = playerItems.indexOfFirst { it.player.isPlaying }
                if (previousIndex != -1) {
                    val player = playerItems[previousIndex].player
                    player.stop()
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
