package com.example.week7soal2.repository

import com.example.week7soal2.model.Album
import com.example.week7soal2.model.Artist
import com.example.week7soal2.model.Track
import com.example.week7soal2.service.AudioDBService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicRepository {

    private val service: AudioDBService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.theaudiodb.com/api/v1/json/123/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AudioDBService::class.java)
    }

    suspend fun searchArtist(name: String): List<Artist> {
        val response = service.searchArtist(name)
        return response.artists ?: emptyList()
    }

    suspend fun searchAlbums(artistName: String): List<Album> {
        val response = service.searchAlbum(artistName)
        return response.album ?: emptyList()
    }

    suspend fun getAlbumDetail(albumId: String): Album? {
        val response = service.getAlbumDetail(albumId)
        return response.album?.firstOrNull()
    }

    suspend fun getAlbumTracks(albumId: String): List<Track> {
        val response = service.getAlbumTracks(albumId)
        return response.track ?: emptyList()
    }
}