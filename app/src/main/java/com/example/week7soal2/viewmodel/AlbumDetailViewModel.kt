package com.example.week7soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7soal2.model.Album
import com.example.week7soal2.model.Track
import com.example.week7soal2.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel : ViewModel() {

    private val repository = MusicRepository()

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadAlbumDetail(albumId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val albumDetail = repository.getAlbumDetail(albumId)
                if (albumDetail == null) {
                    _errorMessage.value = "Detail album tidak ditemukan"
                } else {
                    _album.value = albumDetail
                    val trackList = repository.getAlbumTracks(albumId)
                    _tracks.value = trackList
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry(albumId: String) {
        loadAlbumDetail(albumId)
    }
}