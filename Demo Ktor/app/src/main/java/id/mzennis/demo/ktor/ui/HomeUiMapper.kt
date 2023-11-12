package id.mzennis.demo.ktor.ui

import android.text.Html
import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.model.item.RssStandardItem

class HomeUiMapper {

    fun map(response: RssStandardChannel): HomeUiState {
        return HomeUiState(
            title = response.title.orEmpty(),
            link = response.link.orEmpty(),
            description = response.description.orEmpty(),
            articles = mapArticle(response.items.orEmpty())
        )
    }

    private fun mapArticle(response: List<RssStandardItem>): List<Article> {
        return response.map { article ->
            Article(
                title = article.title.orEmpty(),
                description = if (article.description != null) {
                    Html.fromHtml(article.description, Html.FROM_HTML_MODE_COMPACT).toString()
                } else "",
                link = article.link.orEmpty(),
                imageUrl = article.enclosure?.url.orEmpty(),
            )
        }
    }
}
