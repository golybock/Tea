package com.example.tea.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.ArticleItemRecyclerViewAdapter
import com.example.tea.api.Api
import com.example.tea.databinding.FragmentHomeBinding
import com.example.tea.models.article.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class HomeFragment : Fragment() {

    lateinit var adapter: ArticleItemRecyclerViewAdapter
    private lateinit var articlesRv: RecyclerView

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    lateinit var nothingShow : TextView

    lateinit var list : RecyclerView

    var articles : List<Article>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        nothingShow = binding.homeNothingShow
        list = binding.homeList

    loadArticlesAsync()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter(articles: List<Article>) {
        adapter = ArticleItemRecyclerViewAdapter(articles, activity)
        articlesRv = binding.homeList
        articlesRv.adapter = adapter
    }

    private fun loadArticlesAsync(){
        val th = loadArticles()
        th.start()
        th.join()

        // создаем адаптер
        if (articles != null) {
            initAdapter(articles!!)
        }
        else{
            nothingShow.text = "Нет публикаций"
            nothingShow.visibility = TextView.VISIBLE
            list.visibility = TextView.GONE
        }
    }

    private fun loadArticles(): Thread {
        val thread = Thread{
            val  api : Api = Api(activity)

            try {
                val httpUrlConnection = URL("http://188.164.136.18:8888" + "/api/Article/getArticles").openConnection() as HttpURLConnection
                httpUrlConnection.apply {
                    connectTimeout = 10000 // 10 seconds
                    requestMethod = "GET"
                    doInput = true
                }
                if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                    articles = null
                }
                val streamReader = InputStreamReader(httpUrlConnection.inputStream)
                var text: String = ""
                streamReader.use {
                    text = it.readText()
                }

                val type = object : TypeToken<List<Article>>(){}.type

                articles = Gson().fromJson<List<Article>>(text, type)

                httpUrlConnection.disconnect()
            }
            catch (e : IOException){

            }
        }
        return thread
    }
}