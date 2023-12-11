package com.dertefter.ficus.view.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.view.fragments.messages.MessagesTabContainerFragment

class ChatListAdapter(val fragment: MessagesTabContainerFragment) : RecyclerView.Adapter<ChatListAdapter.ChatItemViewHolder>() {
    private var chatItemList = mutableListOf<StudentStudyChatItem>()


    fun setChatItemList(list: List<StudentStudyChatItem>?){
        if (list.isNullOrEmpty()){
            chatItemList.clear()
        }else{
            chatItemList = list.toMutableList()
        }
        notifyDataSetChanged()
    }

    class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val send_by: TextView = itemView.findViewById(R.id.send_by)
        val last_message_text: TextView = itemView.findViewById(R.id.last_message_text)
        val last_message_title: TextView = itemView.findViewById(R.id.last_message_title)
        val is_new: ImageView = itemView.findViewById(R.id.is_new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_item, parent, false)
        return ChatItemViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val currentItem = chatItemList[position]
        holder.is_new.setImageResource(R.drawable.rounded_background)
        holder.send_by.text = currentItem.send_by
        holder.last_message_title.text = currentItem.messages[currentItem.messages.size - 1].title
        holder.last_message_text.text = currentItem.messages[currentItem.messages.size - 1].text
        if (currentItem.is_new){
            holder.is_new.visibility = View.VISIBLE
            holder.send_by.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
            holder.last_message_title.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL))
        }else{
            holder.is_new.visibility = View.GONE
            holder.send_by.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))
            holder.last_message_title.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))
        }
        holder.itemView.setOnClickListener {
            fragment.openChatItem(chatItemList[position])
        }

    }

    override fun getItemCount(): Int {
        return chatItemList.size
    }

}