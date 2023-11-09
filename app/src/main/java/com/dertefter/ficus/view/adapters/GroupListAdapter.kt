package com.dertefter.ficus.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.view.fragments.timetable.SearchGroupFragment

class GroupListAdapter(val fragment: SearchGroupFragment) : RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>() {
    private var groupList = mutableListOf<GroupItem>()
    fun setGroupList(newNewsList: List<GroupItem>?){
        if (newNewsList.isNullOrEmpty()){
            groupList.clear()
        }else{
            groupList = newNewsList.toMutableList()
        }
        notifyDataSetChanged()
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_group)
        val isAvailable: Boolean = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val currentItem = groupList[position]
        holder.title.text = currentItem.title
        if (currentItem.isAvailable){
            holder.itemView.setOnClickListener {
                fragment.setGroup(currentItem.title)
            }
        }else{
            holder.itemView.setOnClickListener {
                fragment.notifyAction("Расписание группы не доступно")
            }
        }

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

}