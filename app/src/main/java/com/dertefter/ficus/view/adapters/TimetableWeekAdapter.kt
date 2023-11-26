package com.dertefter.ficus.view.adapters

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.data.timetable.Day
import com.dertefter.ficus.data.timetable.Lesson
import com.dertefter.ficus.view.fragments.timetable.TimetableWeekContainerFragment
import com.google.android.material.color.MaterialColors
import org.w3c.dom.Text
const val DAY_TYPE = 0
const val LESSON_TYPE = 1
const val LAB_TYPE = 2
const val SEMINAR_TYPE = 3
class TimetableWeekAdapter(val fragment: TimetableWeekContainerFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listOfAny = mutableListOf<Any>()
    var isError = false

    fun setDayList(dayList: List<Day>?){
        if (dayList == null){
            return
        }else{
            listOfAny.clear()
            for (day in dayList){
                listOfAny.add(day)
                if (day.lessons != null){
                    for (lesson in day.lessons!!){
                        listOfAny.add(lesson)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val aud: TextView = itemView.findViewById(R.id.aud)
        val person: TextView = itemView.findViewById(R.id.person)
        val type: TextView = itemView.findViewById(R.id.type)
    }

    class LabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val aud: TextView = itemView.findViewById(R.id.aud)
        val person: TextView = itemView.findViewById(R.id.person)
        val type: TextView = itemView.findViewById(R.id.type)
    }

    class SeminarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val aud: TextView = itemView.findViewById(R.id.aud)
        val person: TextView = itemView.findViewById(R.id.person)
        val type: TextView = itemView.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LESSON_TYPE){
            val itemView = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.item_lesson, parent, false)
            LessonViewHolder(itemView)
        }else if (viewType == LAB_TYPE){
            val itemView = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.item_lesson_lab, parent, false)
            LabViewHolder(itemView)
        }else if (viewType == SEMINAR_TYPE){
            val itemView = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.item_lesson_seminar, parent, false)
            SeminarViewHolder(itemView)
        }else{
            val itemView = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.item_day, parent, false)
            DayViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DayViewHolder){
            val currentItem = listOfAny[position] as Day
            if (currentItem.today){
                val typedValue = TypedValue()
                val theme = fragment.requireContext().theme
                theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
                val color = typedValue.data
                holder.title.setTextColor(color)
                fragment.scrollToPosition(position)
            }else{
                val typedValue = TypedValue()
                val theme = fragment.requireContext().theme
                theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, typedValue, true)
                val color = typedValue.data
                holder.title.setTextColor(color)
            }
            holder.title.text = currentItem.title
        } else if (holder is LessonViewHolder){
            val currentItem = listOfAny[position] as Lesson
            holder.title.text = currentItem.title
            holder.time.text = currentItem.time
            if (currentItem.aud.isNullOrEmpty()){
                (holder.aud.parent as View).visibility = View.GONE
            }else{
                holder.aud.text = currentItem.aud
            }

            if (currentItem.person.isNullOrEmpty()){
                (holder.person.parent as View).visibility = View.GONE
            }else{
                holder.person.text = currentItem.person
            }

            if (currentItem.type.isNullOrEmpty()){
                holder.type.visibility = View.GONE
            }else{
                holder.type.text = currentItem.type
                holder.type.visibility = View.VISIBLE
            }
        }else if (holder is LabViewHolder) {
            val currentItem = listOfAny[position] as Lesson
            holder.title.text = currentItem.title
            holder.time.text = currentItem.time
            holder.aud.text = currentItem.aud
            holder.person.text = currentItem.person
            holder.type.text = currentItem.type
        }else if (holder is SeminarViewHolder) {
            val currentItem = listOfAny[position] as Lesson
            holder.title.text = currentItem.title
            holder.time.text = currentItem.time
            holder.aud.text = currentItem.aud
            holder.person.text = currentItem.person
            holder.type.text = currentItem.type
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfAny[position] is Day){
            DAY_TYPE
        }else{
            if ((listOfAny[position] as Lesson).type == "Лабораторная"){
                LAB_TYPE
            }else if ((listOfAny[position] as Lesson).type == "Практика"){
                SEMINAR_TYPE
            }else{
                LESSON_TYPE
            }
        }
    }

    fun generateLessonItemView(lesson: Lesson): View {
        var lessonItem: View
        if (lesson.type == "Лабораторная"){
            lessonItem = LayoutInflater.from(fragment.context).inflate(R.layout.item_lesson_lab, null, false)
        }else if (lesson.type == "Практика"){
            lessonItem = LayoutInflater.from(fragment.context).inflate(R.layout.item_lesson_seminar, null, false)
        }else{
            lessonItem = LayoutInflater.from(fragment.context).inflate(R.layout.item_lesson, null, false)
        }
        lessonItem.findViewById<TextView>(R.id.title).text = lesson.title
        lessonItem.findViewById<TextView>(R.id.time).text = lesson.time
        lessonItem.findViewById<TextView>(R.id.aud).text = lesson.aud
        lessonItem.findViewById<TextView>(R.id.person).text = lesson.person
        lessonItem.findViewById<TextView>(R.id.type).text = lesson.type
        if (lesson.type.isNullOrEmpty()){
            lessonItem.findViewById<TextView>(R.id.type).visibility = View.GONE
        }
        if (lesson.person.isNullOrEmpty()){
            (lessonItem.findViewById<TextView>(R.id.person).parent as LinearLayout).visibility = View.GONE
        }
        if (lesson.aud.isNullOrEmpty()){
            (lessonItem.findViewById<TextView>(R.id.aud).parent as LinearLayout).visibility = View.GONE
        }

        return lessonItem
    }


    override fun getItemCount(): Int {
        return listOfAny.size
    }

}