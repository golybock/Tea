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
import java.io.IOException
import java.util.*


class SearchFragment : Fragment() {

    lateinit var adapter: ArticleItemRecyclerViewAdapter
    private lateinit var articlesRv: RecyclerView

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

        loadArticles()

        val search = binding.searchView

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    loadArticles(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                if (newText != null) {
                    loadArticles(newText)
                }
                return false
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter(articles: List<Article>) {
        adapter = ArticleItemRecyclerViewAdapter(articles, activity)
        articlesRv = binding.list
        articlesRv.adapter = adapter
    }

    private fun loadArticles() {
        val  api : Api = Api(activity)

        var articles : List<Article>? = null

        try {
            articles = api.getArticles()

            // создаем адаптер
            if (articles != null) {
                initAdapter(articles)
                return
            }

            binding.homeNothingShow.text = "Нет публикаций"
            binding.homeNothingShow.visibility = TextView.VISIBLE
            binding.list.visibility = TextView.GONE

        }
        catch (e : IOException){
            binding.homeNothingShow.text = "Ничего не найдено"
            binding.homeNothingShow.visibility = TextView.VISIBLE
            binding.list.visibility = TextView.GONE
        }
    }

    private fun loadArticles(search: String) {
        val  api : Api = Api(activity)

        var articles : List<Article>? = null

        try {
            articles = api.getArticles(search)

            // создаем адаптер
            if (articles != null) {
                initAdapter(articles)
                return
            }

            binding.homeNothingShow.text = "Нет публикаций"
            binding.homeNothingShow.visibility = TextView.VISIBLE
            binding.list.visibility = TextView.GONE

        }
        catch (e : IOException){
            binding.homeNothingShow.text = "Ничего не найдено"
            binding.homeNothingShow.visibility = TextView.VISIBLE
            binding.list.visibility = TextView.GONE
        }
    }
}
