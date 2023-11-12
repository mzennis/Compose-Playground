package id.mzennis.demo.ktor.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.parser.RssStandardParser

/**
 * Created by meyta.taliti on 12/11/23.
 */
class CloudService(private val httpClient: HttpClient) {

    suspend fun getArticles(
        urlString: String = "https://typealias.com/index.xml"
    ): RssStandardChannel {
        val response = httpClient.get(urlString).bodyAsText()
        return RssStandardParser().parse(response)
    }
}