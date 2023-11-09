package com.dertefter.ficus.view.fragments.timetable

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.timetable.Timetable
import com.dertefter.ficus.data.timetable.Week
import com.dertefter.ficus.databinding.FragmentTimetableBinding
import com.dertefter.ficus.databinding.FragmentWeekTimetableContainerBinding
import com.dertefter.ficus.viewmodel.timetable.TimetableViewModel

class TimetableWeekContainerFragment : Fragment(R.layout.fragment_week_timetable_container) {

    lateinit var timetableViewModel: TimetableViewModel
    var binding: FragmentWeekTimetableContainerBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekTimetableContainerBinding.bind(view)
        timetableViewModel = ViewModelProvider(this)[TimetableViewModel::class.java]
        observeGetPosts()
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
        //todo
    }

    private fun onLoading() {
        //TODO
    }

}

