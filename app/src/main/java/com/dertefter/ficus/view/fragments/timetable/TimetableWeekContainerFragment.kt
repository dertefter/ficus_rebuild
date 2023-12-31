package com.dertefter.ficus.view.fragments.timetable

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.databinding.FragmentWeekTimetableContainerBinding
import com.dertefter.ficus.view.adapters.TimetableWeekAdapter
import com.dertefter.ficus.viewmodel.timetable.TimetableFragmentWeekSelectorViewModel
import com.dertefter.ficus.viewmodel.timetable.TimetableWeekViewModel
import com.google.android.material.snackbar.Snackbar

class TimetableWeekContainerFragment() : Fragment(R.layout.fragment_week_timetable_container) {

    lateinit var timetableViewModel: TimetableWeekViewModel
    var binding: FragmentWeekTimetableContainerBinding? = null
    var timetableWeekAdapter: TimetableWeekAdapter? = null
    var weekQuery: String? = null

    override fun onResume() {
        super.onResume()
        binding?.daysRecyclerView?.scrollToPosition(0)
        (parentFragment as TimetableFragmentWeekSelector).binding?.appBarLayout?.liftOnScrollTargetViewId = binding?.daysRecyclerView?.id!!
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekTimetableContainerBinding.bind(view)
        timetableViewModel = ViewModelProvider(this)[TimetableWeekViewModel::class.java]
        timetableWeekAdapter = TimetableWeekAdapter(this)
        timetableViewModel.initWeekDao(requireActivity().application)
        observeGetPosts()
        setupRecyclerView()
        weekQuery = arguments?.getString("weekQuery")
        showTimetableForWeek(weekQuery)
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        val recyclerView: RecyclerView = binding?.daysRecyclerView!!
        recyclerView.adapter = timetableWeekAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        recyclerView.addItemDecoration(itemDecorator)

    }


    private fun showTimetableForWeek(weekQuery: String?) {
        if (weekQuery != null){
            timetableViewModel.getTimetableForWeek(weekQuery)
        }
    }

    private fun observeGetPosts() {
        timetableViewModel.weeksLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data, it.additionalInfo)
                Status.ERROR -> onError(it.error)
            }
        }
    }

    private fun onError(error: Error?) {
        binding?.progressBar?.visibility = View.GONE
        binding?.errorView?.visibility = View.VISIBLE
        binding?.daysRecyclerView?.visibility = View.GONE
        binding?.errorTextview?.text = error?.title
        binding?.retryButton?.setOnClickListener {
            timetableViewModel.getTimetableForWeek(weekQuery!!)
        }
    }
    private fun onSuccess(data: List<Week>?, additionalInfo: String? = null) {
        if (!additionalInfo.isNullOrEmpty()){
            val snackbar = Snackbar.make(requireView(), additionalInfo, Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
        binding?.daysRecyclerView?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
        if (data != null){
            for (week in data){
                if (week.weekQuery == weekQuery){
                    if (timetableWeekAdapter?.itemCount == 0){
                        ObjectAnimator.ofFloat(binding?.daysRecyclerView, "alpha", 0f, 1f).setDuration(300).start()
                    }
                    val days = week.timetable?.days
                    timetableWeekAdapter?.setDayList(days)
                }
            }

        }

    }

    private fun onLoading() {
        binding?.errorView?.visibility = View.GONE
        if (timetableWeekAdapter?.itemCount == 0){
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.daysRecyclerView?.visibility = View.GONE
        }

    }

    fun scrollToPosition(position: Int) {
        binding?.daysRecyclerView?.smoothScrollToPosition(position)
    }

}

