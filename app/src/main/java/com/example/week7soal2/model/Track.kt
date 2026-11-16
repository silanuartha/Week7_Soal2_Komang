package com.example.week7soal2.model

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("idTrack") val idTrack: String,
    @SerializedName("strTrack") val strTrack: String,
    @SerializedName("intTrackNumber") val intTrackNumber: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("intDuration") val intDuration: String?
)

data class TrackResponse(
    @SerializedName("track") val track: List<Track>?
)