package com.dertefter.ficus.view.fragments.messages

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dertefter.ficus.R
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.data.messages.StudentStudyMessage
import com.dertefter.ficus.databinding.FragmentMoreMessagesBinding
import com.dertefter.ficus.view.adapters.MoreMessagesAdapter
import com.dertefter.ficus.viewmodel.messages.MessagesViewModel
import com.google.android.material.shape.MaterialShapeDrawable


class MessagesMoreFragment : Fragment(R.layout.fragment_more_messages) {

    var binding: FragmentMoreMessagesBinding? = null
    lateinit var messagesViewModel: MessagesViewModel
    var adapter: MoreMessagesAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoreMessagesBinding.bind(view)
        adapter = MoreMessagesAdapter(this)
        messagesViewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        val chatItem = MessagesMoreFragmentArgs.fromBundle(requireArguments()).ChatItem
        setupAppBar(chatItem)
        setupRecyclerView(chatItem)
    }

    private fun setupAppBar(chatItem: StudentStudyChatItem){
        binding?.toolbar?.title = chatItem.send_by
        binding?.appBarLayout?.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(context)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView(chatItem: StudentStudyChatItem){
        val recyclerView = binding?.recyclerView
        recyclerView?.adapter = adapter
        adapter?.setData(chatItem.messages)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        recyclerView?.addItemDecoration(itemDecorator)


    }

    fun openMessage(currentItem: StudentStudyMessage) {
        val chatItem = MessagesMoreFragmentArgs.fromBundle(requireArguments()).ChatItem
        val messages  = chatItem.messages
        for (message in messages){
            if (message.mes_id == currentItem.mes_id){
                message.is_new = false
            }
        }
        chatItem.messages = messages
        arguments?.putParcelable("ChatItem", chatItem)
        findNavController().navigate(MessagesMoreFragmentDirections.actionReadMessagesFragmentToReadMessageFragment(currentItem))
    }


}