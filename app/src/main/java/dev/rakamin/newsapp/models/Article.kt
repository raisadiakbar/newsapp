package dev.rakamin.newsapp.models

data class Article(
    val title: String,
    val description: String,
    val author: String,
    val publishedAt: String,
    val urlToImage: String
)
