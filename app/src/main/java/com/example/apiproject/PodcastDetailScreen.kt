package com.example.apiproject

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun PodcastDetailScreen(podcast: SearchForTermQuery.PodcastSeries) {
    val context = LocalContext.current
    val currentPodcast = rememberUpdatedState(podcast)

    // Initialize DBHandler
    val dbHandler = DBHandler(context)

    fun openWebsite() {
        val websiteUrl = currentPodcast.value.websiteUrl
        if (!websiteUrl.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            context.startActivity(intent)
        }
    }

    fun sharePodcast() {
        val shareContent = buildString {
            append("Check out this podcast: ${podcast.name}\n")
            append("Description: ${podcast.description}\n")
            podcast.websiteUrl?.let { append("Website: $it\n") }
        }
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareContent)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Podcast"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = podcast.imageUrl),
            contentDescription = "Podcast Cover Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = podcast.name ?: "No title found",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = podcast.description ?: "No description found",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Episodes: ${podcast.totalEpisodesCount}")
        Text(text = "Genre: ${(podcast.genres ?: "NOT_SPECIFIED")}")
        Text(text = "Language: ${podcast.language}")
        Text(text = "Content Type: ${podcast.contentType}")
        Text(text = "Author: ${podcast.authorName}")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { openWebsite() }) {
            Text("Visit Website")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to add to favorites
        Button(onClick = { dbHandler.addFavorite(podcast) }) {
            Text("Add to Favorites")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Share button
        Button(onClick = { sharePodcast() }) {
            Text("Share Podcast")
        }
    }
}
