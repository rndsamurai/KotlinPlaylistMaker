package com.practicum.kotlinplaylistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val artistTimeTextView: TextView = itemView.findViewById(R.id.artistTimeTextView)
    private var trackimage: ImageView = itemView.findViewById(R.id.trackimage)


    fun bind(model: Track){
        nameTextView.text = model.trackName
        artistTextView.text = model.artistName
        artistTimeTextView.text = model.trackTime

        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(trackimage)
    }

}