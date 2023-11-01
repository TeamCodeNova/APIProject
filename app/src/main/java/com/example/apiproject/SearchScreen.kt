package com.example.apiproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.apollographql.apollo3.exception.ApolloException
import com.example.apiproject.SearchForTermQuery as SearchQuery
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(onNavigate: (podcast: SearchQuery.PodcastSeries) -> Unit) {
    var query by remember { mutableStateOf("") }
    var state by remember { mutableStateOf<SearchState>(SearchState.Empty) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray.copy(alpha = 0.2f))
                .padding(8.dp),
            textStyle = TextStyle.Default.copy(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                state = SearchState.Loading
                try {
                    val response = apolloClient.query(SearchQuery(term = query)).execute()
                    if (response.hasErrors()) {
                        state = SearchState.Error(response.errors!!.first().message)
                    } else {
                        state = SearchState.Success(response.data!!)
                    }
                } catch (e: ApolloException) {
                    state = SearchState.Error(e.localizedMessage ?: "Unknown error")
                }
            }
        }) {
            Text(text = "Search")
        }

        when (val s = state) {
            SearchState.Loading -> CircularProgressIndicator()
            is SearchState.Error -> Text(text = s.message, color = Color.Red)
            is SearchState.Success -> {
                println("Storing to view model: ${s.data.searchForTerm?.podcastSeries?.size}")
                SharedPodcastRepository.podcasts = s.data.searchForTerm?.podcastSeries

                PodcastList(data = s.data.searchForTerm?.podcastSeries, onPodcastClick = { selectedPodcast ->
                    println("Navigating to podcast with UUID: ${selectedPodcast?.uuid}")

                    if (selectedPodcast != null) {
                        onNavigate(selectedPodcast)
                    }
                })
            }

            SearchState.Empty -> {}
        }
    }
}

private sealed interface SearchState {
    object Empty : SearchState
    object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class Success(val data: SearchQuery.Data) : SearchState
}

object SharedPodcastRepository {
    var podcasts: List<SearchQuery.PodcastSeries?>? = null
}
