package com.dertefter.ficus.view.fragments.timetable

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.databinding.FragmentTimetableWeekSelectorBinding
import com.dertefter.ficus.view.adapters.TimetableViewPagerAdapter
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.dertefter.ficus.viewmodel.timetable.TimetableFragmentWeekSelectorViewModel
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class TimetableFragmentWeekSelector : Fragment(R.layout.fragment_timetable_week_selector) {

    lateinit var stateFlowViewModel: StateFlowViewModel
    lateinit var timetableViewModel: TimetableFragmentWeekSelectorViewModel
    var binding: FragmentTimetableWeekSelectorBinding? = null
    var currentWeek: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTimetableWeekSelectorBinding.bind(view)
        timetableViewModel = ViewModelProvider(this)[TimetableFragmentWeekSelectorViewModel::class.java]
        timetableViewModel.initWeekDao(requireActivity().application)
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        binding?.todayWeekFab?.hide()
        observeUiState()
        observeGetPosts()
    }


    fun setupAppbar(title: String = "Расписание занятий", subtitle: String = ""){
        binding?.progressBar?.visibility = View.GONE
        binding?.appBarLayout?.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(context)
        binding?.topAppBar?.title = title
        binding?.topAppBar?.subtitle = subtitle
        binding?.topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.set_group -> {
                    findNavController().navigate(R.id.action_timetableFragment_to_setGroupFragment)
                    timetableViewModel.clearData()
                    true
                }
                else -> false
            }
        }
    }
    fun setupViewPagerAndTabs(weeks: List<Week>?) {
        val viewPager = binding?.timetablePager
        val tabLayout = binding?.weeksTabLayout
        if (viewPager?.adapter == null){
            val adapter = TimetableViewPagerAdapter(childFragmentManager, lifecycle)
            viewPager?.adapter = adapter
            adapter.setWeeks(weeks)
        }else{
            val adapter = viewPager.adapter as TimetableViewPagerAdapter
            adapter.setWeeks(weeks)
        }

        TabLayoutMediator(tabLayout!!, viewPager!!) {
                tab, position ->  tab.text = weeks?.get(position)?.title
                if (weeks?.get(position)?.today == true){
                    currentWeek = position
                    binding?.todayWeekFab?.setOnClickListener {
                        viewPager.setCurrentItem(position, true)
                    }
                    viewPager.setCurrentItem(position, false)
                }
        }.attach()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == currentWeek){
                    binding?.todayWeekFab?.hide()
                } else{
                    binding?.todayWeekFab?.show()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun updateWeeks(weeks: List<Week>?){
        if (!weeks.isNullOrEmpty()){
            Log.e("TimetableFragmentWeekSelector", "updateWeeks: $weeks")
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

    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.timetableData == null){
                    stateFlowViewModel.updateTimetableData()
                }else{
                    if (it.timetableData?.group.isNullOrEmpty() || it.timetableData?.groupTitle.isNullOrEmpty()){
                        showGroupNotSet()
                    }else{
                        if (it.isAuthrized == true){
                            setupAppbar(it.timetableData?.groupTitle!!, it.timetableData?.group!!)
                            timetableViewModel = ViewModelProvider(this@TimetableFragmentWeekSelector)[TimetableFragmentWeekSelectorViewModel::class.java]
                            timetableViewModel.initWeekDao(requireActivity().application)
                            timetableViewModel.getWeekList()
                        }else{
                            setupAppbar(it.timetableData?.groupTitle!!)
                            timetableViewModel = ViewModelProvider(this@TimetableFragmentWeekSelector)[TimetableFragmentWeekSelectorViewModel::class.java]
                            timetableViewModel.initWeekDao(requireActivity().application)
                            timetableViewModel.getWeekList()
                        }

                    }
                }
            }
        }
    }

    private fun showGroupNotSet() {
        setupAppbar("Расписание занятий")
        binding?.groupNotSet?.visibility = View.VISIBLE
        binding?.setGroupButton?.setOnClickListener {
            findNavController().navigate(R.id.action_timetableFragment_to_setGroupFragment)
        }
    }

    private fun onError(error: Error?) {
        binding?.progressBar?.visibility = View.GONE
    }
    private fun onSuccess(data: List<Week>?) {
        binding?.progressBar?.visibility = View.GONE
        updateWeeks(data)
    }

    private fun onLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

}

