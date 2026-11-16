package com.example.week7soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.week7soal2.model.Album
import com.example.week7soal2.model.Artist
import com.example.week7soal2.viewmodel.ArtistViewModel

val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkCard = Color(0xFF2A2A2A)
val GreenAccent = Color(0xFF1DB954)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB3B3B3)
val TextMuted = Color(0xFF6B6B6B)

@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    artistViewModel: ArtistViewModel = viewModel()
) {
    val searchQuery by artistViewModel.searchQuery.collectAsState()
    val artist by artistViewModel.artist.collectAsState()
    val albums by artistViewModel.albums.collectAsState()
    val isLoading by artistViewModel.isLoading.collectAsState()
    val errorMessage by artistViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GreenAccent,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Music Artist",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = { artistViewModel.onSearchQueryChange(it) },
            onSearch = { artistViewModel.searchArtist() },
            onClear = { artistViewModel.clearSearch() }
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenAccent)
                }
            }
            errorMessage != null -> {
                ErrorContent(
                    message = errorMessage!!,
                    onRetry = { artistViewModel.searchArtist() }
                )
            }
            artist != null -> {
                ArtistContent(
                    artist = artist!!,
                    albums = albums,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(text = "Cari artis...", color = TextMuted)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextSecondary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Hapus",
                        tint = TextSecondary
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = GreenAccent,
            unfocusedBorderColor = TextMuted,
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            cursorColor = GreenAccent
        ),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
            onSearch = { onSearch() }
        )
    )
}

@Composable
fun ArtistContent(
    artist: Artist,
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = artist.strArtistBanner ?: artist.strArtistThumb,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = artist.strArtistThumb,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(50))
                        .background(DarkSurface),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = artist.strArtist,
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = buildString {
                            append(artist.strGenre ?: "")
                            if (!artist.strCountry.isNullOrEmpty()) {
                                if (artist.strGenre != null) append(" · ")
                                append(artist.strCountry)
                            }
                        },
                        color = GreenAccent,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "Tentang Artis",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            val bio = artist.strBiographyEN ?: "Biografi tidak tersedia"
            Text(
                text = bio,
                color = TextSecondary,
                fontSize = 13.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isExpanded) "Tutup ringkasan" else "Baca selengkapnya",
                color = GreenAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Diskografi Album",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Koleksi rilisan resmi",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GreenAccent
            ) {
                Text(
                    text = "${albums.size} Album",
                    color = Color.Black,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(((albums.size / 2 + albums.size % 2) * 220).dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(albums) { album ->
                AlbumCard(album = album, onClick = { onAlbumClick(album.idAlbum) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column {
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = album.strAlbum,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = album.intYearReleased ?: "",
                        color = GreenAccent,
                        fontSize = 11.sp
                    )
                    Text(
                        text = album.strGenre ?: "",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = Color(0xFFB00020).copy(alpha = 0.15f),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "!", color = Color(0xFFCF6679), fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Terjadi Kendala",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onRetry,
            border = androidx.compose.foundation.BorderStroke(1.dp, GreenAccent),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GreenAccent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Coba Lagi", color = GreenAccent)
        }
    }
}