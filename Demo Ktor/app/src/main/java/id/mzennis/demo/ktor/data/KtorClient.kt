package id.mzennis.demo.ktor.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

/**
 * Created by meyta.taliti on 12/11/23.
 */
val ktorClient = HttpClient(CIO)