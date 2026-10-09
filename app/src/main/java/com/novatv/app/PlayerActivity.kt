package com.novatv.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.novatv.app.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private var exoPlayer: ExoPlayer? = null
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater); setContentView(binding.root)
        binding.channelLabel.text = intent.getStringExtra("name") ?: "NOVA TV"
        val url = intent.getStringExtra("url")
        if (url.isNullOrBlank()) { finish(); return }
        exoPlayer = ExoPlayer.Builder(this).build().also { player ->
            binding.playerView.player = player
            player.setMediaItem(MediaItem.fromUri(url))
            player.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    binding.channelLabel.text = "Flux indisponible : vérifie le lien ou sa compatibilité"
                }
            })
            player.prepare(); player.playWhenReady = true
        }
    }
    override fun onStop() { super.onStop(); exoPlayer?.release(); exoPlayer = null }
}
