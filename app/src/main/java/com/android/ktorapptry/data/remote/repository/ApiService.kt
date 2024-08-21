package com.android.ktorapptry.data.remote.repository

import com.android.ktorapptry.data.remote.KtorApiClient
import com.android.ktorapptry.data.remote.Movie
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ApiService (private val client: HttpClient){
    companion object {
        private const val END_POINT =
            "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink"
    }
    suspend fun getDrinks() = client.get(END_POINT)
}


class MovieRepository(private val apiClient: KtorApiClient) {

    suspend fun getMovies(): List<Movie> {
        return apiClient.
    }
}