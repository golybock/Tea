package com.example.tea

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.tea.placeholder.PlaceholderContent.PlaceholderItem
import com.example.tea.databinding.ArtcileFragmentItemBinding
import com.example.tea.models.Article

class ArticleItemRecyclerViewAdapter(
    private val values: MutableList<Article>
) : RecyclerView.Adapter<ArticleItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ArtcileFragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.headerView.text = item.title
        holder.contentView.text = item.description
        holder.userNameView.text = item.login
//        holder.articleImageView
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ArtcileFragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val headerView: TextView = binding.articleText
        val contentView: TextView = binding.articleHeader
        val userNameView: TextView = binding.userNameHeader
        val articleImageView : ImageView = binding.articleImage

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}