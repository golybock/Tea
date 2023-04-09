package com.example.tea.ui.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.ArticleItemRecyclerViewAdapter
import com.example.tea.api.Api
import com.example.tea.databinding.FragmentSearchBinding
import com.example.tea.models.article.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class SearchFragment : Fragment() {

    lateinit var adapter: ArticleItemRecyclerViewAdapter
    private lateinit var articlesRv: RecyclerView

    var articles : List<Article>? = null

    lateinit var nothingShow : TextView

    lateinit var list : RecyclerView

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val search = binding.searchView
        val clear = binding.clearSearchButton

        nothingShow = binding.homeNothingShow
        list = binding.list

        loadArticlesAsync()

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if(query.isEmpty()){
                        loadArticlesAsync()
                    }
                    loadArticlesAsync(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {
                    if(newText.isEmpty()){
                        loadArticlesAsync()
                    }
                    else{
                        loadArticlesAsync(newText)
                    }
                }
                return false
            }
        })

        clear.setOnClickListener {
            search.setQuery("", true)
            loadArticles()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter(articles: List<Article>) {
        adapter = ArticleItemRecyclerViewAdapter(articles, activity)
        articlesRv = list
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

    private fun loadArticlesAsync(search: String){
        val th = loadArticles(search)
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

    private fun loadArticles(search: String): Thread {
        val thread = Thread{
            val  api : Api = Api(activity)

            var articles : List<Article>? = null

            try {
                val httpUrlConnection = URL("http://188.164.136.18:8888" + "/api/Article/getArticleByAuthor" + search).openConnection() as HttpURLConnection
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
