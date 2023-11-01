package com.example.apiproject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PodcastList(data: List<SearchForTermQuery.PodcastSeries?>?, onPodcastClick: (SearchForTermQuery.PodcastSeries?) -> Unit) {
    LazyColumn {
        items(data.orEmpty()) { podcast ->
            PodcastListItem(podcast = podcast, onPodcastClick = onPodcastClick)
        }
    }
}

@Composable
fun PodcastListItem(podcast: SearchForTermQuery.PodcastSeries?, onPodcastClick: (SearchForTermQuery.PodcastSeries?) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                podcast?.let {
                    onPodcastClick(it)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = podcast?.name.orEmpty(), style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "UUID: ${podcast?.uuid}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "RSS NAME: ${podcast?.rssOwnerName}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
