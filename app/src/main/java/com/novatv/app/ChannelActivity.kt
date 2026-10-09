
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

        val b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val category = intent.getStringExtra("category")
            ?: "📺 Toutes les chaînes"

        b.pageTitle.text = category

        // Chaînes intégrées + chaînes importées par playlist M3U
        val all = (
            OfficialCatalog.channels + PlaylistStore.load(this)
        ).distinctBy { it.url }

        val filtered = when {
            category.contains("Toutes les chaînes", true) ->
                all

            category.contains("Maroc", true) ->
                all.filter {
                    it.group.contains("maroc", true) ||
                    it.group.contains("morocco", true)
                }

            category.contains("Algérie", true) ->
                all.filter {
                    it.group.contains("alg", true) ||
                    it.group.contains("algeria", true)
                }

            category.contains("Égypte", true) ->
                all.filter {
                    it.group.contains("egypt", true) ||
                    it.group.contains("égypte", true)
                }

            category.contains("France", true) ->
                all.filter {
                    it.group.contains("france", true) ||
                    it.group.contains("français", true) ||
                    it.name.contains("France 24", true)
                }

            category.contains("Royaume-Uni", true) ||
            category.contains("USA", true) ->
                all.filter {
                    it.group.contains("english", true) ||
                    it.group.contains("usa", true) ||
                    it.group.contains("uk", true) ||
                    it.group.contains("united states", true)
                }

            category.contains("Favoris", true) ->
                emptyList()

            else ->
                all.filter {
                    it.group.contains(category.substringAfter(' ').trim(), true) ||
                    it.group.contains(category, true)
                }
        }

        val names = if (filtered.isEmpty()) {
            listOf(
                "Aucune chaîne dans cette catégorie",
                "Importer / remplacer la playlist M3U",
                "Toutes les chaînes (${all.size})"
            )
        } else {
            filtered.map { it.name }
        }

        b.grid.layoutManager = GridLayoutManager(this, 4)

        b.grid.adapter = TileAdapter(names) { selected ->
            when {
                selected.startsWith("Importer") -> {
                    Toast.makeText(
                        this,
                        "Reviens à l'accueil puis choisis Importer playlist M3U",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }

                selected.startsWith("Toutes les chaînes") -> {
                    startActivity(
                        Intent(this, ChannelActivity::class.java)
                            .putExtra("category", "📺 Toutes les chaînes")
                    )
                    finish()
                }

                else -> {
                    val channel = filtered.firstOrNull {
                        it.name == selected
                    }

                    if (channel != null) {
                        startActivity(
                            Intent(this, PlayerActivity::class.java)
                                .putExtra("name", channel.name)
                                .putExtra("url", channel.url)
                        )
                    }
                }
            }
        }

        b.grid.requestFocus()
    }
}
