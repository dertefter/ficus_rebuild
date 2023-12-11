package com.dertefter.ficus.view.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.messages.StudentStudyMessage
import com.dertefter.ficus.view.fragments.messages.MessagesMoreFragment

class MoreMessagesAdapter(val fragment: MessagesMoreFragment) : RecyclerView.Adapter<MoreMessagesAdapter.ChatItemViewHolder>() {
    private var data = mutableListOf<StudentStudyMessage>()


    fun setData(list: List<StudentStudyMessage>?){
        if (list.isNullOrEmpty()){
            data.clear()
        }else{
            data = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message_text: TextView = itemView.findViewById(R.id.last_message_text)
        val message_title: TextView = itemView.findViewById(R.id.last_message_title)
        val is_new: ImageView = itemView.findViewById(R.id.is_new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_item_more, parent, false)
        return ChatItemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val currentItem = data[position]
        holder.is_new.setImageResource(R.drawable.rounded_background)
        holder.message_title.text = currentItem.title
        holder.message_text.text = currentItem.text
        if (currentItem.is_new){
            holder.is_new.visibility = View.VISIBLE
            holder.message_title.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
        }else{
            holder.is_new.visibility = View.GONE
            holder.message_title.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))
        }

        holder.itemView.setOnClickListener {
            holder.is_new.visibility = View.GONE
            holder.message_title.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))
            fragment.openMessage(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}