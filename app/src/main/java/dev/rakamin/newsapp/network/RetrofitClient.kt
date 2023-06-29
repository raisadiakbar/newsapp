package dev.rakamin.newsapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsAPI: NewsAPI by lazy {
        retrofit.newBuilder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("X-Api-Key", NewsInterface.API_KEY)
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
            .create(NewsAPI::class.java)
    }
}
