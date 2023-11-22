package com.example.apiproject

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apiproject.type.Genre
import com.example.apiproject.type.Language
import com.example.apiproject.type.PodcastContentType

@Composable
fun Favorites() {
    val context = LocalContext.current
    val dbHandler = remember { DBHandler(context) }
    val favorites = remember { mutableStateListOf<SearchForTermQuery.PodcastSeries>() }

    LaunchedEffect(Unit) {
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

    LazyColumn {
        items(favorites) { podcast ->
            FavoritePodcastItem(podcast, dbHandler)
        }
    }
}

@Composable
fun FavoritePodcastItem(podcast: SearchForTermQuery.PodcastSeries, dbHandler: DBHandler) {
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
                podcast.uuid?.let { dbHandler.deleteFavorite(it) }
                // Update UI accordingly
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
        Language.values().find { it.name == it.toString().trim() }
    }
}

fun parseContentType(contentTypeString: String?): PodcastContentType? {
    return contentTypeString?.let {
        PodcastContentType.values().find { it.name == it.toString().trim() }
    }
}
