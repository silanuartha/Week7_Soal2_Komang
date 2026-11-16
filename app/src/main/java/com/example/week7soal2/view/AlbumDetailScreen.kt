package com.example.week7soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.week7soal2.model.Track
import com.example.week7soal2.viewmodel.AlbumDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumId: String,
    onBack: () -> Unit,
    albumDetailViewModel: AlbumDetailViewModel = viewModel()
) {
    val album by albumDetailViewModel.album.collectAsState()
    val tracks by albumDetailViewModel.tracks.collectAsState()
    val isLoading by albumDetailViewModel.isLoading.collectAsState()
    val errorMessage by albumDetailViewModel.errorMessage.collectAsState()

    LaunchedEffect(albumId) {
        albumDetailViewModel.loadAlbumDetail(albumId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Album",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = GreenAccent)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Memuat detail album & track list...",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        message = errorMessage!!,
                        onRetry = { albumDetailViewModel.retry(albumId) }
                    )
                }
            }
            album != null -> {
                var isDescExpanded by remember { mutableStateOf(false) }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        AsyncImage(
                            model = album!!.strAlbumThumb,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(horizontal = 32.dp, vertical = 16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = album!!.strAlbum,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = album!!.strArtist ?: "",
                                color = GreenAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (!album!!.intYearReleased.isNullOrEmpty()) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = DarkSurface
                                    ) {
                                        Text(
                                            text = "Tahun ${album!!.intYearReleased}",
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                if (!album!!.strGenre.isNullOrEmpty()) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = DarkSurface
                                    ) {
                                        Text(
                                            text = album!!.strGenre!!,
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            Text(
                                text = "Deskripsi Album",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            val desc = album!!.strDescriptionEN ?: "Deskripsi tidak tersedia"
                            Text(
                                text = desc,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                maxLines = if (isDescExpanded) Int.MAX_VALUE else 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            androidx.compose.material3.TextButton(
                                onClick = { isDescExpanded = !isDescExpanded },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = if (isDescExpanded) "Tutup" else "Baca selengkapnya",
                                    color = GreenAccent,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎵", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Daftar Lagu",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenAccent
                            ) {
                                Text(
                                    text = "${tracks.size} Lagu",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        HorizontalDivider(color = DarkSurface, thickness = 1.dp)
                    }

                    items(tracks) { track ->
                        TrackItem(track = track)
                        HorizontalDivider(
                            color = DarkSurface,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = String.format("%02d", track.intTrackNumber?.toIntOrNull() ?: 0),
            color = GreenAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(32.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.strTrack,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.strGenre ?: "",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
        val duration = track.intDuration?.toLongOrNull()
        if (duration != null) {
            val minutes = duration / 60000
            val seconds = (duration % 60000) / 1000
            Text(
                text = "⏱ ${String.format("%02d:%02d", minutes, seconds)}",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}