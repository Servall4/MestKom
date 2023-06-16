package com.example.mestkom.ui.video

import androidx.media3.exoplayer.ExoPlayer

data class PlayerItem(
    val player: ExoPlayer,
    val position: Int
)