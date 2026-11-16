package com.example.week7soal2.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("idAlbum") val idAlbum: String,
    @SerializedName("strAlbum") val strAlbum: String,
    @SerializedName("strArtist") val strArtist: String?,
    @SerializedName("intYearReleased") val intYearReleased: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strDescriptionEN") val strDescriptionEN: String?,
    @SerializedName("strAlbumThumb") val strAlbumThumb: String?
)

data class AlbumResponse(
    @SerializedName("album") val album: List<Album>?
)