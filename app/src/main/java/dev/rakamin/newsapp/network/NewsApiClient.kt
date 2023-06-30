package dev.rakamin.newsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.*


object NewsApiClient {
    private const val BASE_URL = "https://newsapi.org"

    val newsInterface: NewsInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsInterface = retrofit.create(NewsInterface::class.java)
    }
}
