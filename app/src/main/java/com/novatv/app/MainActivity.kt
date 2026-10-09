package com.novatv.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.novatv.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val categories = listOf(
        "🇲🇦 Maroc", "🇩🇿 Algérie", "🇪🇬 Égypte", "🇫🇷 France", "🇬🇧 Royaume-Uni", "🇺🇸 USA",
        "🎬 Cinéma arabe & égyptien", "🎬 Cinéma français", "🎬 Cinéma international",
        "🌍 Documentaires arabes", "🌍 Documentaires français", "🌍 Documentaires monde",
        "🎵 Musique arabe", "🎵 Musique française", "🎵 Musique internationale",
        "📰 Actualités", "⚽ Sport", "👶 Jeunesse", "⭐ Favoris", "🛰️ Satellites", "📂 Importer playlist M3U", "📺 Toutes les chaînes"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val logoAnim = ScaleAnimation(0.92f, 1f, 0.92f, 1f, 1, 0.5f, 1, 0.5f).apply { duration = 700; fillAfter = true }
        binding.root.startAnimation(AlphaAnimation(0.65f, 1f).apply { duration = 650 })
        binding.grid.layoutManager = GridLayoutManager(this, 4)
        binding.grid.adapter = TileAdapter(categories) { category ->
            when {
                category.startsWith("🛰") -> startActivity(Intent(this, SatelliteActivity::class.java))
                category.startsWith("📂") -> openPlaylistPicker()
                else -> startActivity(Intent(this, ChannelActivity::class.java).putExtra("category", category))
            }
        }
        binding.grid.requestFocus()
    }
    private fun openPlaylistPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply { addCategory(Intent.CATEGORY_OPENABLE); type = "*/*" }
        startActivityForResult(intent, 401)
    }
    @Deprecated("Legacy result API retained for Android 5 compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 401 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try { PlaylistStore.import(this, uri); startActivity(Intent(this, ChannelActivity::class.java).putExtra("category", "📺 Toutes les chaînes")) }
                catch (e: Exception) { android.widget.Toast.makeText(this, "Impossible de lire cette playlist", android.widget.Toast.LENGTH_LONG).show() }
            }
        }
    }
}
