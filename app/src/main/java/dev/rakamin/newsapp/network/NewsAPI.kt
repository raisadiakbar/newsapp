package dev.rakamin.newsapp.network

import dev.rakamin.newsapp.models.NewsModel
import retrofit2.Call
import retrofit2.http.GET

interface NewsAPI {
    @GET("v2/top-headlines")
    fun getHeadlines(): Call<List<NewsModel>>

    @GET("v2/everything")
    fun getAllNews(): Call<List<NewsModel>>
}