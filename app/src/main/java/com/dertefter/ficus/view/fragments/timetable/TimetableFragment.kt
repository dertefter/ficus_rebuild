package com.dertefter.ficus.view.fragments.timetable

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.databinding.FragmentTimetableBinding
import com.dertefter.ficus.view.adapters.timetableViewPagerAdapter
import com.dertefter.ficus.viewmodel.timetable.TimetableViewModel
import com.google.android.material.tabs.TabLayoutMediator

class TimetableFragment : Fragment(R.layout.fragment_timetable) {

    lateinit var timetableViewModel: TimetableViewModel
    var binding: FragmentTimetableBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTimetableBinding.bind(view)
        timetableViewModel = ViewModelProvider(this)[TimetableViewModel::class.java]

        if (timetableViewModel.getGroup().isNullOrEmpty()){
            binding?.groupNotSet?.visibility = View.VISIBLE
            binding?.setGroupButton?.setOnClickListener {
                findNavController().navigate(R.id.action_timetableFragment_to_setGroupFragment)
            }
        } else{
            binding?.groupNotSet?.visibility = View.GONE
            observeGetPosts()
            timetableViewModel.getWeeks()
        }

    }

    fun setupViewPagerAndTabs(weeks: List<Week>) {
        val viewPager = binding?.timetablePager
        val tabLayout = binding?.weeksTabLayout
        val adapter = timetableViewPagerAdapter(activity?.supportFragmentManager!!, lifecycle)
        viewPager?.adapter = adapter
        adapter.setWeeks(weeks)
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = weeks[position].title
        }.attach()

    }

    private fun updateWeeks(weeks: List<Week>?){
        if (weeks != null) {
            setupViewPagerAndTabs(weeks)
        }
    }

    private fun observeGetPosts() {
        timetableViewModel.weeksLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
            }
        }
    }

    private fun onError(error: Error?) {
        //TODO
    }
    private fun onSuccess(data: List<Week>?) {
        updateWeeks(data)
    }

    private fun onLoading() {
        //TODO
    }

}

