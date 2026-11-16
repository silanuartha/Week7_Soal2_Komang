package com.example.week7soal2.service

import com.example.week7soal2.model.AlbumResponse
import com.example.week7soal2.model.ArtistResponse
import com.example.week7soal2.model.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioDBService {

    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbum(@Query("s") artistName: String): AlbumResponse

    @GET("album.php")
    suspend fun getAlbumDetail(@Query("m") albumId: String): AlbumResponse

    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): TrackResponse
}