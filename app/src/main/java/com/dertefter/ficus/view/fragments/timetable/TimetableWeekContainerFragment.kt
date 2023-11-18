package com.dertefter.ficus.view.fragments.timetable

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.databinding.FragmentWeekTimetableContainerBinding
import com.dertefter.ficus.view.adapters.TimetableWeekAdapter
import com.dertefter.ficus.viewmodel.timetable.TimetableViewModel

class TimetableWeekContainerFragment() : Fragment(R.layout.fragment_week_timetable_container) {

    lateinit var timetableViewModel: TimetableViewModel
    var binding: FragmentWeekTimetableContainerBinding? = null
    var timetableWeekAdapter: TimetableWeekAdapter? = null
    var weekQuery: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekTimetableContainerBinding.bind(view)
        timetableViewModel = ViewModelProvider(requireActivity())[TimetableViewModel::class.java]
        timetableWeekAdapter = TimetableWeekAdapter(this)
        observeGetPosts()
        setupRecyclerView()
        weekQuery = arguments?.getString("weekQuery")
        showTimetableForWeek(weekQuery)
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
        timetableViewModel.timetableLiveData.observe(viewLifecycleOwner) {
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
    private fun onSuccess(data: Map<String, Timetable?>?) {
        Log.e("eee", data.toString())
        binding?.progressBar?.visibility = View.GONE
        if (data != null){
            val timetable = data[weekQuery]
            val days = timetable?.days
            timetableWeekAdapter?.setDayList(days)
            binding?.progressBar?.visibility = View.GONE
            binding?.daysRecyclerView?.visibility = View.VISIBLE
        }

    }

    private fun onLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.daysRecyclerView?.visibility = View.GONE
    }

}

