package com.practicum.kotlinplaylistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val artistTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val artistTimeTextView: TextView = itemView.findViewById(R.id.artistTimeTextView)
    private var trackimage: ImageView = itemView.findViewById(R.id.trackimage)

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())


    fun bind(model: Track){
        nameTextView.text = model.trackName
        artistTextView.text = model.artistName
        artistTimeTextView.text = formatter.format(model.trackTimeMillis)

        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(trackimage)

    }
}
