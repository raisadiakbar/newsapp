package dev.rakamin.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.rakamin.newsapp.adapters.NewsAdapter
import dev.rakamin.newsapp.models.Article
import dev.rakamin.newsapp.models.NewsResponse
import dev.rakamin.newsapp.network.NewsApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewTopHeadlines: RecyclerView
    private lateinit var recyclerViewAllNews: RecyclerView
    private lateinit var topHeadlinesAdapter: NewsAdapter
    private lateinit var allNewsAdapter: NewsAdapter
    private var topHeadlinesList: MutableList<Article> = mutableListOf()
    private var allNewsList: MutableList<Article> = mutableListOf()
    private var currentPage = 1
    private var isFetching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTopHeadlines = findViewById(R.id.recyclerViewTopHeadlines)
        recyclerViewAllNews = findViewById(R.id.recyclerViewAllNews)

        recyclerViewTopHeadlines.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAllNews.layoutManager = LinearLayoutManager(this)

        topHeadlinesAdapter = NewsAdapter(topHeadlinesList)
        allNewsAdapter = NewsAdapter(allNewsList)

        recyclerViewTopHeadlines.adapter = topHeadlinesAdapter
        recyclerViewAllNews.adapter = allNewsAdapter

        fetchTopHeadlines()
        setupScrollListener()
    }

    private fun fetchTopHeadlines() {
        val apiKey = "6c7e12d410ca4d1897335e5b296b437f"
        val country = "id"

        isFetching = true
        val call = NewsApiClient.newsInterface.getTopHeadlines(apiKey, country)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val articles = newsResponse?.articles ?: emptyList()
                    topHeadlinesList.addAll(articles)
                    topHeadlinesAdapter.notifyDataSetChanged()
                }
                isFetching = false
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                isFetching = false
                Toast.makeText(this@MainActivity, "Failed to fetch top headlines: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun fetchAllNews(query: String) {
        val apiKey = "6c7e12d410ca4d1897335e5b296b437f"

        isFetching = true
        val call = NewsApiClient.newsInterface.getAllNews(apiKey, query, currentPage, 20)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    val articles = newsResponse?.articles ?: emptyList()
                    allNewsList.addAll(articles)
                    allNewsAdapter.notifyDataSetChanged()
                }
                isFetching = false
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                isFetching = false
                Toast.makeText(this@MainActivity, "Failed to fetch all news: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setupScrollListener() {
        val layoutManagerAllNews = recyclerViewAllNews.layoutManager as LinearLayoutManager
        recyclerViewAllNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isFetching && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val visibleItemCount = layoutManagerAllNews.childCount
                    val totalItemCount = layoutManagerAllNews.itemCount
                    val firstVisibleItemPosition = layoutManagerAllNews.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        currentPage++
                        fetchAllNews("your_query_here")
                    }
                }
            }
        })
    }
}
