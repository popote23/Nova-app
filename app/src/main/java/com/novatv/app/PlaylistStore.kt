package com.novatv.app

import android.content.Context
import android.net.Uri

object PlaylistStore {
    private const val PREFS = "nova_prefs"
    private const val KEY = "channels"
    fun load(context: Context): List<Channel> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "") ?: ""
        return parse(raw)
    }
    fun import(context: Context, uri: Uri) {
        val text = context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, text).apply()
    }
    private fun parse(text: String): List<Channel> {
        val out = mutableListOf<Channel>()
        var name = "Chaîne sans nom"
        var group = "Autres"
        var logo: String? = null
        var tvgId: String? = null
        text.lineSequence().forEach { rawLine ->
            val line = rawLine.trim()
            if (line.startsWith("#EXTINF", true)) {
                name = line.substringAfter(',', "Chaîne sans nom").trim().ifBlank { "Chaîne sans nom" }
                group = Regex("group-title=\"([^\"]+)\"", RegexOption.IGNORE_CASE).find(line)?.groupValues?.get(1) ?: "Autres"
                logo = Regex("tvg-logo=\"([^\"]+)\"", RegexOption.IGNORE_CASE).find(line)?.groupValues?.get(1)
                tvgId = Regex("tvg-id=\"([^\"]+)\"", RegexOption.IGNORE_CASE).find(line)?.groupValues?.get(1)
            } else if (line.isNotBlank() && !line.startsWith("#") && (line.startsWith("http://") || line.startsWith("https://"))) {
                out.add(Channel(name, group, line, logo, tvgId))
                name = "Chaîne sans nom"; group = "Autres"; logo = null; tvgId = null
            }
        }
        return out
    }
}
