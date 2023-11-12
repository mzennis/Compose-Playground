package id.mzennis.demo.ktor.ui

import androidx.compose.runtime.Immutable

/**
 * Created by meyta.taliti on 12/11/23.
 */
data class HomeUiState(
    val title: String,
    val link: String,
    val description: String,
    val articles: List<Article>
)

@Immutable
data class Article(
    val title: String,
    val description: String,
    val link: String,
    val imageUrl: String
)