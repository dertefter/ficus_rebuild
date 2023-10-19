package com.dertefter.ficus.utils.netiCore

import android.animation.ObjectAnimator
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dertefter.ficus.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ResponseParser {
    fun parseNews(input: ResponseBody?): JSONArray? {

        try{
            val outputJsonArray = JSONArray()
            val jsonString = input!!.string()
            val jsonObject = JSONObject(jsonString)
            val items = jsonObject.getString("items")
            Log.e("ResponseParserJJJJ", jsonObject.toString())
            val doc: Document = Jsoup.parse(items.replace("\n", "").replace("\t", ""))
            val news_items = doc.body().select("a")
            for (it in news_items){
                var imageUrl: String? = null
                if (it.attr("style").toString().replace("background-image: url(", "").replace(");", "").replace("//", "/") != ""){
                    imageUrl = "https://www.nstu.ru/" + it.attr("style").toString().replace("background-image: url(", "").replace(");", "").replace("//", "/")

                }


                val title = it.select("div.main-events__item-title").text().toString()
                val tag = it.select("div.main-events__item-tags").text().toString()
                val date = it.select("div.main-events__item-date").text().toString()
                val link = "https://www.nstu.ru/" + it.attr("href")
                val item: org.json.JSONObject = org.json.JSONObject()
                item.put("title", title)
                item.put("tag", tag)
                item.put("date", date)
                item.put("imageUrl", imageUrl)
                item.put("link", link)
                outputJsonArray.put(item)
            }
            return outputJsonArray
        } catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }
    }
}