package com.example.week7soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7soal2.model.Album
import com.example.week7soal2.model.Artist
import com.example.week7soal2.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArtistViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchArtist() {
        val query = _searchQuery.value.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _artist.value = null
            _albums.value = emptyList()

            try {
                val artists = repository.searchArtist(query)
                if (artists.isEmpty()) {
                    _errorMessage.value = "Artis tidak ditemukan"
                } else {
                    _artist.value = artists.first()
                    val albumList = repository.searchAlbums(query)
                    _albums.value = albumList
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _artist.value = null
        _albums.value = emptyList()
        _errorMessage.value = null
    }
}