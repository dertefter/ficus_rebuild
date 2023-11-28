package com.dertefter.ficus.utils.netiCore

import android.service.autofill.UserData
import android.util.Log
import com.dertefter.ficus.data.User
import com.dertefter.ficus.data.news.NewsContent
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.data.timetable.Day
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.data.timetable.Lesson
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import okhttp3.ResponseBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ResponseParser {
    fun getQueryString(input: String): String? {
        val regex = Regex("\\?(.+)")
        val matchResult = regex.find(input)

        return matchResult?.groups?.get(1)?.value
    }
    fun parseNews(input: ResponseBody?): List<NewsItem>? {
        val outputNewsList = mutableListOf<NewsItem>()
        try{
            val jsonString = input!!.string()
            val jsonObject = JSONObject(jsonString)
            Log.e("ResponseParser", jsonObject.toString())
            val items = jsonObject.getString("items")
            val haveMore = jsonObject.getBoolean("haveMore")
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
                val link = it.attr("href")
                val type = it.select("div.main-events__item-type").text().toString()
                val newsid = getQueryString(link)!!.replace("idnews=", "")
                val dataid  = it.attr("data-type")
                if (dataid == "video" || dataid == "photo"){
                    continue
                }

                val item = NewsItem(newsid, title, date, imageUrl, tag, type)

                outputNewsList.add(item)
            }
            return outputNewsList
        } catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }
    }

    fun parseIndividualGroup(input: ResponseBody?): String? {
        try{
            val pretty = input?.string()!!
            val doc: Document = Jsoup.parse(pretty)
            var fio = doc.body().select("span.fio").first()!!
            Log.e("fio", fio.toString())
            var fio_arr = fio.text().split(" ")
            var group = fio_arr[fio_arr.size - 1]
            return group
        }
        catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }
    }

    fun parseWeeks(input: ResponseBody?): List<Week>?{
        try{
            val pretty = input!!.string()
            val doc: Document = Jsoup.parse(pretty)
            val weeks_content = doc.select("div.schedule__weeks-content")
            val weeks_a = weeks_content.select("a")
            val week_label_today = doc.select("span.schedule__title-label").text()
            val output = mutableListOf<Week>()
            for (it in weeks_a){
                val title = it.text()
                val query = it.attr("data-week")
                var isToday = false
                if (week_label_today.contains(query)){
                    isToday = true
                }
                val week_item = Week(title, query, isToday)
                output.add(week_item)
            }
            return output
        }catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }

    }
    fun parseNewsMore(input: ResponseBody?): NewsContent? {
        try {
            val pretty = input!!.string().toString()
            val doc: Document = Jsoup.parse(pretty)
            val htmlContent = doc.body().select("div.row")[1].toString()
            val htmlContacts = doc.body().select("div.aside-events__contacts").toString()
            return NewsContent(htmlContent, htmlContacts)
            //displayHtml(s.toString())
        } catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }
    }

    fun parseGroups(input: ResponseBody?): List<GroupItem>? {
        val outputGroupList = mutableListOf<GroupItem>()

        val jsonString = input!!.string()
        val jsonObject = JSONObject(jsonString)
        val items = jsonObject.getString("items")

        val doc: Document = Jsoup.parse(items.replace("\n", "").replace("\t", ""))
        val available_group_items = doc.body().select("a")
        val unavailable_group_items = doc.body().select("span")

        for (it in available_group_items){
            val title = it.text().toString()
            val item = GroupItem(title, false, true)
            outputGroupList.add(item)
        }
        for (it in unavailable_group_items){
            val title = it.text().toString()
            val item = GroupItem(title, false, false)
            outputGroupList.add(item)
        }
        return outputGroupList

    }

    fun parseProfileData(input: ResponseBody?): User? {
        try{
            val pretty = input!!.string()
            val doc: Document = Jsoup.parse(pretty)
            val fullName = doc.select("span.fio").text().split(",")[0]
            val name = doc.select("span.fio").text().split(" ")[1]
            val groupTitle = doc.select("span.fio").text().split(",")[1].replace(" ", "")
            return User(name = name, fullName = fullName, groupTitle = groupTitle)
        } catch (e: Exception){
            return null
        }


    }

    fun parseTimetable(input: ResponseBody?): Timetable? {
        try{
            val output = Timetable()
            val pretty = input?.string()!!
            val dayItems = mutableListOf<Day>()
            val doc: Document = Jsoup.parse(pretty)
            val table_body = doc.body().select("div.schedule__table-body").first()
            val table_days = table_body?.select("> *")
            if (table_days != null) {
                for (it in table_days){
                    val title = it.select("div.schedule__table-day").first()?.ownText().toString()
                    val date = it.select("span.schedule__table-date").text()
                    val dayToday: Boolean = it.select("div.schedule__table-day").first()?.attr("data-today").toBoolean()
                    val dayItem = Day(title, date, null, dayToday)
                    val lessonsItems = mutableListOf<Lesson>()
                    val cell = it.select("div.schedule__table-cell")[1]
                    val lessons = cell.select("> *")
                    for (l in lessons){
                        val time = l.select("div.schedule__table-time").text()
                        val items = l.select("div.schedule__table-item")
                        for (t in items){
                            var lesson_title = t.ownText().replace("·", "").replace(",", "")
                            val type = t.select("span.schedule__table-typework").first()?.ownText()
                            val aud = t.parent()?.parent()?.select("div.schedule__table-class")?.text()
                            var person = ""
                            for (p in t.select("a")){
                                person = person + p.text() + "\n"
                            }
                            if (person != ""){
                                person = person.substring(0, person.length - 1)
                            }
                            if (lesson_title != ""){
                                val lesson_item = Lesson(lesson_title, time, type, aud, person, null)
                                lessonsItems.add(lesson_item)

                            }


                        }
                    }
                    dayItem.lessons = lessonsItems
                    dayItems.add(dayItem)
                }
            }
            output.days = dayItems
            return output
        } catch (e: Exception) {
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }
    }
    fun parseIndividualTimetable(input: ResponseBody?, weekQuery: String): Timetable? {
        try{
            val output = Timetable()
            val pretty = input?.string().toString()
            val dayItems = mutableListOf<Day>()
            val doc: Document = Jsoup.parse(pretty)
            val table_body = doc.body().select("div.schedule__table-body").first()
            val table_days = table_body?.select("> *")

            if (table_days != null) {
                for (it in table_days){
                    val title = it.select("div.schedule__table-day").first()?.ownText().toString()

                    val date = it.select("span.schedule__table-date").text()
                    val dayToday: Boolean = it.select("div.schedule__table-day").first()?.attr("data-today").toBoolean()
                    val dayItem = Day(title, date, null, dayToday)
                    val lessonsItems = mutableListOf<Lesson>()
                    val cell = it.select("div.schedule__table-cell")[1]
                    val lessons = cell.select("> *")
                    for (l in lessons){
                        val time = l.select("div.schedule__table-time").text()
                        val items = l.select("div.schedule__table-item")
                        for (t in items){
                            var incorrect = false
                            var label = t.select("span.schedule__table-label").first()
                            if (label != null && !label.text().isNullOrEmpty()){
                                val labelText = label.text()
                                if (labelText.contains("недели")){
                                    val available_weeks = label.text().split(" ")
                                    if (!available_weeks.contains(weekQuery)){
                                        incorrect = true
                                    }
                                }
                                else if (labelText.contains("по чётным") && weekQuery.toInt() % 2 != 0){
                                    incorrect = true
                                }
                                else if (labelText.contains("по нечётным") && weekQuery.toInt() % 2 == 0){
                                    incorrect = true
                                }



                            }

                            var lesson_title = t.ownText().replace("·", "").replace(",", "")
                            val type = t.select("span.schedule__table-typework").first()?.ownText()
                            val aud = t.parent()?.parent()?.select("div.schedule__table-class")?.text()
                            var person = ""
                            for (p in t.select("a")){
                                person = person + p.text() + "\n"
                            }
                            if (person != ""){
                                person = person.substring(0, person.length - 1)
                            }
                            if (lesson_title != "" && !incorrect){
                                val lesson_item = Lesson(lesson_title, time, type, aud, person, null)
                                lessonsItems.add(lesson_item)
                            }


                        }
                    }
                    dayItem.lessons = lessonsItems
                    dayItems.add(dayItem)

                }
            }
            output.days = dayItems
            return output

        }
        catch (e: Exception){
            return null
        }

    }

    fun parseWeeksForIndividual(input: ResponseBody?): List<Week>? {
        try{
            val weekList_ = mutableListOf<Week>()
            val pretty = input?.string().toString()
            var current_week_query = "1"
            val doc: Document = Jsoup.parse(pretty)
            val label = doc.body().select("span.schedule__title-label").text()
            try{
                current_week_query = label.split(" ")[0]
            }catch (e: Exception){
                Log.e("ResponseParser", e.stackTraceToString().toString())
            }
            for (i in 1..18){
                val week = Week(title = "Неделя $i", query = i.toString())
                if (i.toString() == current_week_query){
                    week.today = true
                }
                weekList_.add(week)
            }
            return weekList_
        } catch (e: Exception){
            Log.e("ResponseParser", e.stackTraceToString().toString())
            return null
        }

    }
}