package com.example.tea.ui.home

import android.os.Bundle
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
import java.io.IOException


class HomeFragment : Fragment() {

    lateinit var adapter: ArticleItemRecyclerViewAdapter
    private lateinit var articlesRv: RecyclerView

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadArticles()

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
            binding.homeList.visibility = TextView.GONE
        }
        catch (e : IOException){
            binding.homeNothingShow.text = "Не удалось загрузить публикации"
            binding.homeNothingShow.visibility = TextView.VISIBLE
            binding.homeList.visibility = TextView.GONE
        }

    }
}