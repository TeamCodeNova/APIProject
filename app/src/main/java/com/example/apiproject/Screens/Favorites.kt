package com.example.apiproject.Screens

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apiproject.type.Genre
import com.example.apiproject.type.Language
import com.example.apiproject.type.PodcastContentType
import androidx.navigation.NavController
import com.example.apiproject.Database.DBHandler
import com.example.apiproject.Navigation.NavigationDestinations
import com.example.apiproject.SearchForTermQuery
import com.example.apiproject.Sessions.UserSessionManager

@Composable
fun Favorites(navController: NavController) {
    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context)
    val dbHandler = remember { DBHandler(context) }
    val favorites = remember { mutableStateListOf<SearchForTermQuery.PodcastSeries>() }

    // Redirect to login if not logged in
    LaunchedEffect(Unit) {
        if (!userSessionManager.isUserLoggedIn()) {
            HomeNav(navController)
        }
    }

    fun refreshFavorites() {
        favorites.clear()
        val cursor = dbHandler.getAllFavorites()
        while (cursor.moveToNext()) {
            val podcast = SearchForTermQuery.PodcastSeries(
                uuid = cursor.getStringSafe(DBHandler.KEY_UUID),
                name = cursor.getStringSafe(DBHandler.KEY_NAME),
                description = cursor.getStringSafe(DBHandler.KEY_DESCRIPTION),
                imageUrl = cursor.getStringSafe(DBHandler.KEY_IMAGE_URL),
                totalEpisodesCount = cursor.getIntSafe(DBHandler.KEY_EPISODES) ?: 0,
                genres = parseGenres(cursor.getStringSafe(DBHandler.KEY_GENRES)),
                language = parseLanguage(cursor.getStringSafe(DBHandler.KEY_LANGUAGE)),
                contentType = parseContentType(cursor.getStringSafe(DBHandler.KEY_CONTENT_TYPE)),
                authorName = cursor.getStringSafe(DBHandler.KEY_AUTHOR),
                websiteUrl = cursor.getStringSafe(DBHandler.KEY_WEBSITE_URL),
                rssOwnerPublicEmail = null,
                rssOwnerName = null
            )
            favorites.add(podcast)
        }
        cursor.close()
    }

    LaunchedEffect(Unit) {
        refreshFavorites()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Your Favorites",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFADD8E6))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color.Gray,
            thickness = 1.dp,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(favorites) { podcast ->
                FavoritePodcastItem(podcast, dbHandler, refreshFavorites = { refreshFavorites() })
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun FavoritePodcastItem(
    podcast: SearchForTermQuery.PodcastSeries,
    dbHandler: DBHandler,
    refreshFavorites: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = podcast.imageUrl),
            contentDescription = "Podcast Image",
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )

        Text(text = podcast.name ?: "No Title", fontSize = 18.sp)
        Text(text = podcast.description ?: "No Description", fontSize = 14.sp, color = Color.Gray)

        Row {
            Button(onClick = {
                podcast.uuid?.let {
                    dbHandler.deleteFavorite(it)
                    refreshFavorites()
                }
            }) {
                Text("Remove")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                podcast.websiteUrl?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(intent)
                }
            }) {
                Text("Visit Website")
            }
        }
    }
}

fun HomeNav(navController: NavController) {
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
}

// Extension functions to safely retrieve data from Cursor
fun Cursor.getStringSafe(columnName: String): String? {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex != -1) getString(columnIndex) else null
}

fun Cursor.getIntSafe(columnName: String): Int? {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex != -1) getInt(columnIndex) else null
}

// Helper functions to parse enum types
fun parseGenres(genresString: String?): List<Genre> {
    return genresString?.split(",")?.mapNotNull { genreString ->
        Genre.values().find { it.name == genreString.trim() }
    } ?: emptyList()
}

fun parseLanguage(languageString: String?): Language? {
    return languageString?.let {
        Language.values().find { lang -> lang.name == it.trim() }
    }
}

fun parseContentType(contentTypeString: String?): PodcastContentType? {
    return contentTypeString?.let {
        PodcastContentType.values().find { type -> type.name == it.trim() }
    }
}
