package dev.rakamin.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.rakamin.newsapp.adapters.NewsAdapter
import dev.rakamin.newsapp.models.NewsModel
import dev.rakamin.newsapp.network.NewsAPI
import dev.rakamin.newsapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsList: List<NewsModel>

    private val newsAPI: NewsAPI by lazy {
        RetrofitClient.newsAPI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        newsList = ArrayList()
        newsAdapter = NewsAdapter(newsList)
        recyclerView.adapter = newsAdapter

        getHeadlines()
        getAllNews()
    }

    private fun getHeadlines() {
        val call: Call<List<NewsModel>> = newsAPI.getHeadlines()
        call.enqueue(object : Callback<List<NewsModel>> {
            override fun onResponse(
                call: Call<List<NewsModel>>,
                response: Response<List<NewsModel>>
            ) {
                if (response.isSuccessful) {
                    val newsData = response.body()
                    newsData?.let {
                        newsList = it
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<NewsModel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch headlines: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAllNews() {
        val call: Call<List<NewsModel>> = newsAPI.getAllNews()
        call.enqueue(object : Callback<List<NewsModel>> {
            override fun onResponse(
                call: Call<List<NewsModel>>,
                response: Response<List<NewsModel>>
            ) {
                if (response.isSuccessful) {
                    val newsData = response.body()
                    newsData?.let {
                        newsList = it
                        newsAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<NewsModel>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch all news: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
