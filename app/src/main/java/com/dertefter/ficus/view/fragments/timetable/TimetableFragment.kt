package com.dertefter.ficus.view.fragments.timetable

import android.graphics.Color
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
import com.dertefter.ficus.databinding.FragmentTimetableBinding
import com.dertefter.ficus.view.adapters.timetableViewPagerAdapter
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.dertefter.ficus.viewmodel.timetable.TimetableViewModel
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class TimetableFragment : Fragment(R.layout.fragment_timetable) {

    lateinit var stateFlowViewModel: StateFlowViewModel
    lateinit var timetableViewModel: TimetableViewModel
    var binding: FragmentTimetableBinding? = null
    var currentWeek: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTimetableBinding.bind(view)
        timetableViewModel = ViewModelProvider(requireActivity())[TimetableViewModel::class.java]
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        binding?.todayWeekFab?.hide()
        setupAppbar()
        observeGroupData()
        observeUiState()
        timetableViewModel.getGroup()
    }


    fun setupAppbar(){
        binding?.appBarLayout?.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(context)
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
        val adapter = timetableViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager?.adapter = adapter
        adapter.setWeeks(weeks)
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
        if (weeks != null) {
            setupViewPagerAndTabs(weeks)
            timetableViewModel.weeksLiveData.removeObservers(viewLifecycleOwner)
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

    private fun observeGroupData() {
        timetableViewModel.groupLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (it.data.isNullOrEmpty()){
                        binding?.groupNotSet?.visibility = View.VISIBLE
                        binding?.setGroupButton?.setOnClickListener {
                            findNavController().navigate(R.id.action_timetableFragment_to_setGroupFragment)
                        }
                    }
                    else{
                        binding?.groupNotSet?.visibility = View.GONE
                        observeGetPosts()

                        if (binding?.weeksTabLayout?.tabCount == 0){
                            timetableViewModel.getWeekList()
                            setupAppbar()
                        }

                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.isAuthrized == true){

                }else{

                }
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

