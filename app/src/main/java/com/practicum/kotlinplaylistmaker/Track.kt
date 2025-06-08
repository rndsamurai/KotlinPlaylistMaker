package com.practicum.kotlinplaylistmaker

data class Track (
    var trackName: String,
    var artistName: String,
    var trackTimeMillis: Long?,
    var artworkUrl100: String
)