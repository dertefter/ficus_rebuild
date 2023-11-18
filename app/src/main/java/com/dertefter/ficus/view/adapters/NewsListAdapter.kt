package com.dertefter.ficus.view.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.utils.ViewUtils
import com.dertefter.ficus.view.fragments.news.NewsFragment
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class NewsListAdapter(val fragment: NewsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var newsList = mutableListOf<NewsItem>()
    var isError = false
    fun setNewsList(newNewsList: List<NewsItem>?){
        if (newNewsList == null){
            return
        }
        newsList = newNewsList.toMutableList()
        notifyDataSetChanged()
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_news)
        val tag: TextView = itemView.findViewById(R.id.tag_news)
        val date: TextView = itemView.findViewById(R.id.date_news)
        val image: ImageView = itemView.findViewById(R.id.image_news)
        val type: TextView = itemView.findViewById(R.id.type_news)
        var newsid: String = ""
        var color = Color.GRAY
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val retryButton: TextView = itemView.findViewById(R.id.retry_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
            NewsViewHolder(itemView)
        } else if (viewType == 2){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false)
            ErrorViewHolder(itemView)
        } else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(itemView)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder){
            val currentItem = newsList[position]
            holder.title.text = currentItem.title
            holder.tag.text = currentItem.tag
            holder.date.text = currentItem.date
            holder.newsid = currentItem.newsid
            holder.type.text = currentItem.type
            if (currentItem.imageUrl != null){
                Picasso.get().load(currentItem.imageUrl).into(holder.image)
                Picasso.get()
                    .load(currentItem.imageUrl)
                    .resize(600,400)
                    .centerCrop()
                    .into(holder.image, object : Callback {
                        override fun onSuccess() {
                            holder.color = ViewUtils().getDominantColor(holder.image.drawable.toBitmap())
                        }

                        override fun onError(ex: Exception) {

                        }
                    })

                holder.image.visibility = View.VISIBLE
            }else{
                holder.image.visibility = View.GONE
                holder.color = Color.GRAY
            }
            ViewCompat.setTransitionName(holder.itemView, "image$position")
            holder.itemView.setOnClickListener {
                fragment.readNews(holder.itemView,
                    "image$position",
                    currentItem.title,
                    holder.newsid,
                    currentItem.date,
                    currentItem.imageUrl,
                    holder.color)
            }
        } else if (holder is LoadingViewHolder){
            if (isError){
                holder.itemView.visibility = View.GONE
                holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }else{
                holder.itemView.visibility = View.VISIBLE
                holder.itemView.layoutParams =
                    RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            }
        }else if (holder is ErrorViewHolder){
            holder.retryButton.setOnClickListener {
                fragment.getNews()
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        Log.e("getItemViewType", "$position $newsList")
        if (position == newsList.size){
            return 1
        }else{
            if (newsList[position].error != null && newsList[position].error != ""){
                isError = true
                return 2
            }else{
                isError = false
            }
            return 0
        }
    }

    override fun getItemCount(): Int {
        return newsList.size + 1
    }

}