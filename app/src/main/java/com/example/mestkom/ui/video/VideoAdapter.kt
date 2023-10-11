package com.example.mestkom.ui.video

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.mestkom.data.responses.CommentResponse
import com.example.mestkom.databinding.ListVideoBinding
import com.example.mestkom.ui.cluster.PlacemarkUserData

class VideoAdapter(
    var context: Context,
    var videos: ArrayList<PlacemarkUserData>,
    private var videoPreparedListener: OnVideoPreparedListener,
    private var videoLoadListener: OnVideoLoadListener,
    private var commentOpenListener: OnOpenCommentListener,
    private val viewModel: VideoViewModel
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    class VideoViewHolder(
        val binding: ListVideoBinding,
        val context: Context,
        var videoPreparedListener: OnVideoPreparedListener,
        val viewModel: VideoViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var player: ExoPlayer
        private lateinit var mediaItem: MediaItem

        fun preparePlayer(idVideo: String) {
            mediaItem = MediaItem.fromUri(idVideo)
            player = ExoPlayer.Builder(context).build()
            player.repeatMode = Player.REPEAT_MODE_ONE
            binding.playerView.player = player
            player.seekTo(0)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
            videoPreparedListener.onVideoPrepared(PlayerItem(player, absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = ListVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener, viewModel)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val model = videos[position]
        holder.binding.videoName.text = model.name
        holder.binding.videoDescription.text = model.description

        var comments: List<CommentResponse> = listOf()

        videoLoadListener.onLoadComment(model.idVideo) {
            comments = it
            holder.binding.commentCounter.text = it.size.toString()
        }

        holder.binding.commentButton.setOnClickListener {
            commentOpenListener.onOpenComment(comments, model.idVideo)
        }

        holder.preparePlayer("http://217.25.93.73:8080/video/" + model.idVideo)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(playerItem: PlayerItem)
    }

    interface OnVideoLoadListener {
        fun onLoadComment(idVideo: String, callback: (List<CommentResponse>) -> Unit)
    }

    interface OnOpenCommentListener {
        fun onOpenComment(comments: List<CommentResponse>, idVideo: String)
    }
}