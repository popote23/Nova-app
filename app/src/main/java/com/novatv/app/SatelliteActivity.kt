package com.novatv.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.novatv.app.databinding.ActivityMainBinding

class SatelliteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.pageTitle.text = "Satellites — guide FTA"

        val sats = listOf(
            "🛰️ Nilesat 7°W",
            "🛰️ Hot Bird 13°E",
            "🛰️ Astra 19.2°E",
            "🛰️ Arabsat 26°E",
            "🛰️ Türksat 42°E",
            "📡 Guide des fréquences Nilesat",
            "📺 Sources officielles marocaines",
            "ℹ️ Réception et chaînes gratuites"
        )

        b.grid.layoutManager = GridLayoutManager(this, 4)

        b.grid.adapter = TileAdapter(sats) { item ->
            when {
                item.contains("Guide des fréquences Nilesat") -> {
                    openUrl(
                        "https://www.lyngsat.com/Nilesat-201-301-and-Eutelsat-7-West-A.html"
                    )
                }

                item.contains("Sources officielles marocaines") -> {
                    openUrl("https://snrtlive.ma/fr/node/4019")
                }

                item.contains("Nilesat") -> {
                    Toast.makeText(
                        this,
                        "Nilesat : la réception directe exige une antenne et un tuner satellite compatibles. Ce menu ne décode pas le signal.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "$item : guide informatif. La disponibilité dépend de l'équipement et des fréquences.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        b.grid.requestFocus()
    }

    private fun openUrl(url: String) {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
            )
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Impossible d'ouvrir le guide externe",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
