package com.example.tea.ui.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tea.ArticleItemRecyclerViewAdapter
import com.example.tea.api.Api
import com.example.tea.databinding.FragmentSearchBinding
import com.example.tea.models.Article
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

        val btn : Button = binding.startDateButton
        btn.setOnClickListener {
            //Inflate the dialog as custom view
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    },
                    year,
                    month,
                    day
                )
            }

            if (dpd != null) {
                dpd.show()
            }
        }

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

            articles = arrayListOf<Article>(Article(1, "aboba", "beboba"), Article(2, "abebo", "дора"))

            initAdapter(articles)

        }
        catch (e : IOException){
            articles = arrayListOf<Article>(Article(1, "aboba", "beboba"), Article(2, "abebo", "дора"))
            initAdapter(articles)
        }
    }
}