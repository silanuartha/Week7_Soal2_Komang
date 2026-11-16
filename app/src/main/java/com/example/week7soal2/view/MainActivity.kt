package com.example.week7soal2.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "artist"
    ) {
        composable("artist") {
            ArtistScreen(
                onAlbumClick = { albumId ->
                    navController.navigate("album_detail/$albumId")
                }
            )
        }
        composable(
            route = "album_detail/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            AlbumDetailScreen(
                albumId = albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}