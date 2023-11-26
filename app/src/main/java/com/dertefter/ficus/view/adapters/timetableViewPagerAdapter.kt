package com.dertefter.ficus.view.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.view.fragments.timetable.TimetableWeekContainerFragment



class timetableViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :

    FragmentStateAdapter(fragmentManager, lifecycle) {
    var weeksList: List<Week>? = null

    fun setWeeks(weeks: List<Week>?){
        weeksList = weeks
    }


    override fun getItemCount(): Int {
        return weeksList?.size ?: 0
    }

    override fun createFragment(position: Int): TimetableWeekContainerFragment {
        val week = weeksList?.get(position)
        val fragment = TimetableWeekContainerFragment()
        val bundle = Bundle()
        bundle.putString("weekQuery", week?.query)
        fragment.arguments = bundle
        return fragment
    }
}