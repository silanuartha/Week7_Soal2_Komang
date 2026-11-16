package com.example.week7soal2.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("idArtist") val idArtist: String,
    @SerializedName("strArtist") val strArtist: String,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strCountry") val strCountry: String?,
    @SerializedName("strBiographyEN") val strBiographyEN: String?,
    @SerializedName("strArtistThumb") val strArtistThumb: String?,
    @SerializedName("strArtistBanner") val strArtistBanner: String?
)

data class ArtistResponse(
    @SerializedName("artists") val artists: List<Artist>?
)