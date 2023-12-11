package com.dertefter.ficus.view.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.FragmentAuthBinding
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
                    withContext(Dispatchers.Main){
                        binding.progressBar.visibility = View.GONE
                    }
                    requireActivity().onBackPressedDispatcher.onBackPressed()

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

