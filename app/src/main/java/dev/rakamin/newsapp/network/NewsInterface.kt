package dev.rakamin.newsapp.network

import dev.rakamin.newsapp.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("/v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String
    ): Call<NewsResponse>

    @GET("/v2/everything")
    fun getAllNews(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<NewsResponse>
}
