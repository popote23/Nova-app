package com.novatv.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.novatv.app.databinding.ActivityMainBinding

class SatelliteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        b.pageTitle.text = "Satellites — guide d'information"
        val sats = listOf("🛰️ Nilesat 7°W", "🛰️ Hot Bird 13°E", "🛰️ Astra 19.2°E", "🛰️ Arabsat 26°E", "🛰️ Eutelsat", "🛰️ Türksat 42°E", "🛰️ Amos", "ℹ️ Paramètres et fréquences")
        b.grid.layoutManager = GridLayoutManager(this, 4)
        b.grid.adapter = TileAdapter(sats) { item ->
            android.widget.Toast.makeText(this, "$item : NOVA TV ne pilote pas le tuner satellite de la box. Cette rubrique est un guide; les flux Internet se chargent via M3U.", android.widget.Toast.LENGTH_LONG).show()
        }
        b.grid.requestFocus()
    }
}
