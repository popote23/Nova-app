package com.novatv.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novatv.app.databinding.ItemTileBinding

class TileAdapter(private val items: List<String>, private val onSelect: (String) -> Unit) : RecyclerView.Adapter<TileAdapter.Holder>() {
    class Holder(val binding: ItemTileBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemTileBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.binding.tileTitle.text = item
        holder.binding.tileTitle.setOnClickListener { onSelect(item) }
        holder.binding.tileTitle.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_UP && (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER || keyCode == android.view.KeyEvent.KEYCODE_ENTER)) { onSelect(item); true } else false
        }
    }
}
