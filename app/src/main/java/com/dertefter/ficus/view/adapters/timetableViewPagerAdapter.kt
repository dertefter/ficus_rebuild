package com.dertefter.ficus.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.view.fragments.timetable.TimetableFragment
import com.dertefter.ficus.view.fragments.timetable.TimetableWeekContainerFragment



class timetableViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :

    FragmentStateAdapter(fragmentManager, lifecycle) {
    var weeksList: List<Week>? = null

    fun setWeeks(weeks: List<Week>){
        weeksList = weeks
    }


    override fun getItemCount(): Int {
        return weeksList?.size ?: 0
    }

    override fun createFragment(position: Int): Fragment {
        return TimetableWeekContainerFragment()
    }
}