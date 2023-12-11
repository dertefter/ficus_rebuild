package com.dertefter.ficus.view.fragments.messages

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.messages.StudentStudyChatItem
import com.dertefter.ficus.databinding.FragmentMessagesTabContainerBinding
import com.dertefter.ficus.view.adapters.ChatListAdapter
import com.dertefter.ficus.viewmodel.messages.MessagesViewModel

class MessagesTabContainerFragment() : Fragment(R.layout.fragment_messages_tab_container) {

    lateinit var messagesViewModel: MessagesViewModel
    var binding: FragmentMessagesTabContainerBinding? = null
    var chatListAdapter: ChatListAdapter? = null

    override fun onPause() {
        super.onPause()
        binding?.recyclerView?.scrollToPosition(0)
    }

    override fun onResume() {
        super.onResume()
        val tab = arguments?.getString("tab")
        if (tab != null) {
            showChatList(tab)
        }
        (parentFragment as MessagesFragment).binding?.appBarLayout?.liftOnScrollTargetViewId = binding?.recyclerView?.id!!
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMessagesTabContainerBinding.bind(view)
        messagesViewModel = ViewModelProvider(this)[MessagesViewModel::class.java]
        chatListAdapter = ChatListAdapter(this)
        setupRecyclerView()
        val tab = arguments?.getString("tab")
        if (tab != null) {
            showChatList(tab)
        }
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        val recyclerView: RecyclerView = binding?.recyclerView!!
        recyclerView.adapter = chatListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        recyclerView.addItemDecoration(itemDecorator)

    }

    fun openChatItem(chatItem: StudentStudyChatItem){
        val direction: NavDirections = MessagesFragmentDirections.actionMessagesFragmentToReadMessagesFragment(chatItem)
        findNavController().navigate(direction)
    }
    private fun showChatList(tab: String) {
        Log.e("tabtab", tab)
        if (tab == "Преподаватели и службы"){
            observeTeacherMessages()
            messagesViewModel.getTeacherMessages(true)
        } else if (tab == "Прочее"){
            observeOtherMessages()
            messagesViewModel.getOtherMessages(true)
        } else if (tab == "DiSpace"){
            //todo
        }
    }
    private fun observeOtherMessages() {
        messagesViewModel.otherMessagesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
            }
        }
    }
    private fun observeTeacherMessages() {
        messagesViewModel.teacherMessagesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
            }
        }
    }

    private fun onError(error: Error?) {
        binding?.progressBar?.visibility = View.GONE
    }
    private fun onSuccess(data: List<StudentStudyChatItem>?) {
        binding?.progressBar?.visibility = View.GONE
        if (chatListAdapter?.itemCount == 0){
            ObjectAnimator.ofFloat(binding?.recyclerView, "alpha", 0f, 1f).setDuration(300).start()
        }
        chatListAdapter?.setChatItemList(data)
    }

    private fun onLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.recyclerView?.visibility = View.GONE
    }


}

