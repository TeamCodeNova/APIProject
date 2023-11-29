package com.example.apiproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.apiproject.type.Genre
import com.example.apiproject.SearchForTermQuery as SearchQuery
import kotlinx.coroutines.launch

enum class SortOrder {
    LATEST, OLDEST
}

@Composable
fun SearchScreen(navController: NavController, onNavigate: (podcast: SearchQuery.PodcastSeries) -> Unit) {
    var query by remember { mutableStateOf("") }
    var state by remember { mutableStateOf<SearchState>(SearchState.Empty) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userSessionManager = UserSessionManager(context)
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }
    var sortByDatePublished by remember { mutableStateOf<SortOrder?>(null) }
    // Redirect to login if not logged in
    LaunchedEffect(Unit) {
        if (!userSessionManager.isUserLoggedIn()) {
            HomeGo(navController)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFAFAFA) // A light background color
    ) {
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

            var showAdvancedOptions by remember { mutableStateOf(false) }

            Button(onClick = { showAdvancedOptions = !showAdvancedOptions }) {
                Text(text = if (showAdvancedOptions) "Hide Advanced Options" else "Show Advanced Options")
            }

            if (showAdvancedOptions) {
                Spacer(modifier = Modifier.height(16.dp))

                Text("Genre", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CustomDropdownMenu(
                    items = Genre.values().toList(),
                    selectedItem = selectedGenre,
                    onItemSelected = { selectedGenre = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Sort Order", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CustomDropdownMenu(
                    items = SortOrder.values().toList(),
                    selectedItem = sortByDatePublished,
                    onItemSelected = { sortByDatePublished = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                scope.launch {
                    state = SearchState.Loading
                    try {
                        val response = apolloClient.query(SearchQuery(
                            term = query,
                            filterForGenres = Optional.presentIfNotNull(selectedGenre?.let { listOf(it) }),
                            sortByDatePublished = Optional.presentIfNotNull(sortByDatePublished?.let {
                                com.example.apiproject.type.SortOrder.valueOf(it.name)
                            }),
                        )).execute()
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
                    SharedPodcastRepository.podcasts = s.data.searchForTerm?.podcastSeries

                    PodcastList(data = s.data.searchForTerm?.podcastSeries, onPodcastClick = { selectedPodcast ->
                        if (selectedPodcast != null) {
                            onNavigate(selectedPodcast)
                        }
                    })
                }

                SearchState.Empty -> {}
            }
        }
    }
}

fun HomeGo(navController: NavController) {
    navController.navigate(NavigationDestinations.LOGIN_SCREEN)
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

@Composable
fun <T> CustomDropdownMenu(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedItem?.toString() ?: "Select an option",
                color = Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {Text(text = item.toString())},
                    onClick = {
                    onItemSelected(item)
                    expanded = false
                })
            }
        }
    }
}
