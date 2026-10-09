package com.novatv.app

import android.content.Intent
import android.net.Uri
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

        val imported = PlaylistStore.load(this)
        val all = imported + OfficialCatalog.channels

        val filtered = when {
            category.contains("Toutes les chaînes", true) -> all

            category.contains("Favoris", true) -> emptyList()

            else -> {
                val keys = keywordsFor(category)

                all.filter { channel ->
                    keys.any { key ->
                        channel.name.contains(key, true) ||
                        channel.group.contains(key, true)
                    }
                }
            }
        }

        val names = if (filtered.isEmpty()) {
            listOf(
                "Aucune chaîne disponible dans cette catégorie",
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
                        "Retourne à l'accueil pour importer une playlist M3U",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }

                selected.startsWith("Toutes les chaînes") -> {
                    startActivity(
                        Intent(this, ChannelActivity::class.java)
                            .putExtra(
                                "category",
                                "📺 Toutes les chaînes"
                            )
                    )
                }

                selected.startsWith("Aucune chaîne") -> {
                    Toast.makeText(
                        this,
                        "Importe une playlist autorisée ou consulte les sources officielles.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    val channel = filtered.firstOrNull {
                        it.name == selected
                    }

                    if (channel != null) {
                        if (channel.group.contains("Officiel", true)) {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(channel.url)
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this,
                                    "Impossible d'ouvrir le site officiel",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            startActivity(
                                Intent(this, PlayerActivity::class.java)
                                    .putExtra("name", channel.name)
                                    .putExtra("url", channel.url)
                            )
                        }
                    }
                }
            }
        }

        b.grid.requestFocus()
    }

    private fun keywordsFor(category: String): List<String> {
        return when {
            category.contains("Maroc", true) ->
                listOf("maroc", "morocco")

            category.contains("Algérie", true) ->
                listOf("algérie", "algerie", "algeria", "alg")

            category.contains("Égypte", true) ->
                listOf("égypte", "egypte", "egypt", "masr")

            category.contains("Pays arabes", true) ->
                listOf("arabe", "arab", "arabic")

            category.contains("Cinéma arabe", true) ->
                listOf("cinéma arabe", "cinema arab", "egypt", "film arab")

            category.contains("Cinéma français", true) ->
                listOf("cinéma français", "cinema french", "france")

            category.contains("Cinéma international", true) ->
                listOf("cinéma", "cinema", "film", "movie")

            category.contains("Documentaires arabes", true) ->
                listOf("documentaire arabe", "documentary arab", "arab")

            category.contains("Documentaires français", true) ->
                listOf("documentaire français", "documentary french", "france")

            category.contains("Documentaires monde", true) ->
                listOf("documentaire", "documentary")

            category.contains("Musique arabe", true) ->
                listOf("musique arabe", "arabic music", "arab music")

            category.contains("Musique française", true) ->
                listOf("musique française", "french music")

            category.contains("Musique internationale", true) ->
                listOf("musique", "music")

            category.contains("Actualités", true) ->
                listOf("actualités", "news", "information", "journal")

            category.contains("Sport", true) ->
                listOf("sport", "sports")

            category.contains("Jeunesse", true) ->
                listOf("jeunesse", "kids", "children", "enfant")

            category.contains("Royaume-Uni", true) ->
                listOf("royaume-uni", "united kingdom", "british", "uk")

            category.contains("USA", true) ->
                listOf("usa", "united states", "american", "américain")

            category.contains("France", true) ->
                listOf("france", "français", "french")

            else -> listOf(category.substringAfter(' ').trim())
        }
    }
}
