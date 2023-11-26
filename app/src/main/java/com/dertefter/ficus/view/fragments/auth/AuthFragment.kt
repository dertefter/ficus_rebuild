package com.dertefter.ficus.view.fragments.auth

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
import com.dertefter.ficus.databinding.FragmentAuthBinding
import com.dertefter.ficus.databinding.FragmentTimetableBinding
import com.dertefter.ficus.view.adapters.timetableViewPagerAdapter
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.dertefter.ficus.viewmodel.timetable.TimetableViewModel
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch


class AuthFragment : Fragment(R.layout.fragment_auth) {
    lateinit var binding: FragmentAuthBinding
    private lateinit var stateFlowViewModel: StateFlowViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        binding = FragmentAuthBinding.bind(view)
        observeUiState()
        binding.authButton.setOnClickListener {
            stateFlowViewModel.tryAuth(binding.loginTextField.editText?.text.toString(), binding.passwordTextField.editText?.text.toString())
        }

    }

    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.isAuthrized == true){
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    binding.progressBar.visibility = View.GONE
                }else if (it.isAuthrized == false){
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Произошла ошибка...", Snackbar.LENGTH_SHORT).show()
                }else{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }



}

