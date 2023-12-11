package com.dertefter.ficus.view.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.view.fragments.messages.MessagesTabContainerFragment
import com.dertefter.ficus.view.fragments.timetable.TimetableWeekContainerFragment



class messagesViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :

    FragmentStateAdapter(fragmentManager, lifecycle) {
    var tabList = mutableListOf<String>("Преподаватели и службы", "Прочее", "DiSpace")


    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): MessagesTabContainerFragment {
        val tab = tabList[position]
        val fragment = MessagesTabContainerFragment()
        val bundle = Bundle()
        bundle.putString("tab", tab)
        fragment.arguments = bundle
        return fragment
    }
}