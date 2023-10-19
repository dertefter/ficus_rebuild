package com.dertefter.ficus.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class NewsListAdapter(private val newsList: JSONArray?) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title_news)
        val tag = itemView.findViewById<TextView>(R.id.tag_news)
        val date = itemView.findViewById<TextView>(R.id.date_news)
        val image = itemView.findViewById<ImageView>(R.id.image_news)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList?.getJSONObject(position)!!
        holder.title.text = (currentItem).getString("title")
        holder.tag.text = (currentItem).getString("tag")
        holder.date.text = (currentItem).getString("date")
        if ((currentItem).has("imageUrl")){
            Picasso.get().load((currentItem).getString("imageUrl")).into(holder.image)
            holder.image.visibility = View.VISIBLE
        }else{
            holder.image.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        if (newsList == null){
            return 0
        }
        return newsList?.length()!!
    }
}