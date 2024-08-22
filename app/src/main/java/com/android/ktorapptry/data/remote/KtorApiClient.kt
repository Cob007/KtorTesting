package com.android.ktorapptry.data.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val NETWORK_TIME_OUT = 6_000L
private const val BASE_URL = "ktor.io"


class KtorApiClient {

    //Add Engine : CIO, ANDROID, IOS
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation){
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }
        install(HttpTimeout){
            requestTimeoutMillis = NETWORK_TIME_OUT
            connectTimeoutMillis = NETWORK_TIME_OUT
            socketTimeoutMillis = NETWORK_TIME_OUT
        }

        install(Logging){
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    suspend fun getMovies(): List<Movie> {
        val response : HttpResponse = httpClient.get("https://api.example.com/users"){
            header(HttpHeaders.Accept, true)
        }
        return response.body()
    }

    //Another format
    suspend fun getMoviesWithCustomUrl() : List<Movie> {
        val response : HttpResponse = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
                path("docs/welcome.html")
            }
        }
        return response.body()
    }

    //Another format
    suspend fun getMoviesUsingRequest() : List<Movie> {
        val response: HttpResponse = httpClient.request(BASE_URL) {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
                path("docs/welcome.html")
            }
        }
        return response.body()


    }

}

