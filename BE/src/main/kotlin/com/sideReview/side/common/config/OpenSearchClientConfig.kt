package com.sideReview.side.common.config

import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.Node
import com.jillesvangurp.ktsearch.SearchClient
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenSearchClientConfig {
    @Bean
    public fun openSearchClient(): SearchClient {
        val client =
            SearchClient(
                KtorRestClient(
                    https = false,
                    user = "uwho",
                    password = "Uwho1234!",
                    nodes = arrayOf(Node("15.164.189.220", 9200))
                )
            )
        runBlocking {
            val engineInfo = client.engineInfo()
            println("**** Open Search Client connection ****")
            println(engineInfo.name)
            println(engineInfo.clusterName)
            println(engineInfo.version.number)
            println("****************************************")
        }
        return client
    }
}