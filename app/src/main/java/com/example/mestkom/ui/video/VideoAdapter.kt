package com.example.mestkom.ui.video

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.mestkom.databinding.ListVideoBinding
import com.example.mestkom.ui.cluster.PlacemarkUserData

class VideoAdapter(
    var context: Context,
    var videos: ArrayList<PlacemarkUserData>,
    private var videoPreparedListener: OnVideoPreparedListener,
    private var videoLoadListener: OnVideoLoadListener,
    private val viewModel: VideoViewModel
): RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(
        val binding: ListVideoBinding,
        val context: Context,
        var videoPreparedListener: OnVideoPreparedListener,
        val viewModel: VideoViewModel
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var player: ExoPlayer
        private lateinit var mediaItem: MediaItem

        fun preparePlayer(filePath: String) {
            mediaItem = MediaItem.fromUri(filePath)
            player = ExoPlayer.Builder(context).build()
            player.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = player
            player.seekTo(0)
            player.setMediaItem(mediaItem)
            player.prepare()

            if (absoluteAdapterPosition == 0) {
                player.playWhenReady = true
                player.play()
            }
            videoPreparedListener.onVideoPrepared(PlayerItem(player, absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = ListVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener, viewModel)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Toast.makeText(context, "Binded ${position}", Toast.LENGTH_LONG).show()
        val model = videos[position]
        holder.binding.videoName.text = model.name
        holder.binding.videoDescription.text = model.description
        videoLoadListener.onLoadVideo(model.idVideo, holder.binding) { response ->
            holder.preparePlayer(response)
        }
        viewModel.downloadVideo(model.idVideo)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(playerItem: PlayerItem)
    }

    interface OnVideoLoadListener {
        fun onLoadVideo(idVideo: String, listBinding: ListVideoBinding, callback: (String) -> Unit)
    }
}