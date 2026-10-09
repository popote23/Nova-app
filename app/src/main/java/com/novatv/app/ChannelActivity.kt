package com.novatv.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.novatv.app.databinding.ActivityMainBinding

class ChannelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        val category = intent.getStringExtra("category") ?: "📺 Toutes les chaînes"
        b.pageTitle.text = category
        val all = PlaylistStore.load(this)
        val filtered = when {
            category.contains("Toutes les chaînes") -> all
            category.contains("Maroc") -> all.filter { it.group.contains("maroc", true) || it.group.contains("morocco", true) }
            category.contains("Algérie") -> all.filter { it.group.contains("alg", true) }
            category.contains("Égypte") -> all.filter { it.group.contains("egypt", true) || it.group.contains("égypte", true) }
            category.contains("France") -> all.filter { it.group.contains("france", true) || it.group.contains("français", true) }
            category.contains("Royaume-Uni") || category.contains("USA") -> all.filter { it.group.contains("english", true) || it.group.contains("usa", true) || it.group.contains("uk", true) }
            category.contains("Favoris") -> emptyList()
            else -> all.filter { it.group.contains(category.substringAfter(' ').trim(), true) || it.group.contains(category, true) }
        }
        val names = if (filtered.isEmpty()) listOf("Aucune chaîne dans cette catégorie", "Importer / remplacer la playlist M3U", "Toutes les chaînes (${all.size})") else filtered.map { it.name }
        b.grid.layoutManager = GridLayoutManager(this, 4)
        b.grid.adapter = TileAdapter(names) { selected ->
            when {
                selected.startsWith("Importer") -> { Toast.makeText(this, "Reviens à l'accueil puis choisis Importer playlist M3U", Toast.LENGTH_LONG).show(); finish() }
                selected.startsWith("Toutes les chaînes") -> { startActivity(Intent(this, ChannelActivity::class.java).putExtra("category", "📺 Toutes les chaînes")); finish() }
                filtered.isNotEmpty() -> { val ch = filtered.firstOrNull { it.name == selected }; if (ch != null) startActivity(Intent(this, PlayerActivity::class.java).putExtra("name", ch.name).putExtra("url", ch.url)) }
            }
        }
        b.grid.requestFocus()
    }
}
