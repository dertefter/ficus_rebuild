package com.dertefter.ficus.view.fragments.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.FragmentProfileDialogBinding
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileDialogFragment : DialogFragment(R.layout.fragment_profile_dialog) {

    var binding: FragmentProfileDialogBinding? = null
    lateinit var stateFlowViewModel: StateFlowViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileDialogBinding.bind(view)
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        observeUiState()
        stateFlowViewModel.getName()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.isAuthrized == true){
                    showAuthCard(false)
                }else{
                    showAuthCard(true)
                }
                binding?.name?.text = it.user?.name
            }
        }
    }

    fun showAuthCard(v: Boolean){
        if (v){
            binding?.authCard?.visibility = View.VISIBLE
            runTextAnimation()
            binding?.buttonLogin?.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_authFragment)
                dismiss()
            }
        }else{
            binding?.authCard?.visibility = View.GONE
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun runTextAnimation(){
        val strings = arrayOf("Сообщения от преподавателей",
            "Запись в бюро пропусков",
            "Зачётка",
            "Читательский билет",
            "Сообщения от деканата",
            "Поиск курсов DiSpace",
            "Заказ справки об обучении",
            "Диалоги DiSpace",
            "И многое другое!")

        CoroutineScope(Dispatchers.Main).launch {
            while (true){
                for (i in strings.indices) {
                    binding?.animatedTextView?.animateText(strings[i])
                    delay(3000)
                }

            }
        }

    }

}