package com.dertefter.ficus.view.fragments.messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.FragmentMessagesBinding
import com.dertefter.ficus.view.adapters.messagesViewPagerAdapter
import com.dertefter.ficus.viewmodel.messages.MessagesViewModel
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MessagesFragment : Fragment(R.layout.fragment_messages) {

    var binding: FragmentMessagesBinding? = null
    lateinit var stateFlowViewModel: StateFlowViewModel
    lateinit var messagesViewModel: MessagesViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMessagesBinding.bind(view)
        binding?.appBarLayout?.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(context)
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        observeUiState()
        setupViewPagerAndTabs()

    }

    fun setupViewPagerAndTabs() {
        val viewPager = binding?.messagesViewPager
        val tabLayout = binding?.tabLayout
        val adapter = messagesViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            tab.text = (viewPager.adapter as messagesViewPagerAdapter).tabList[position]
        }.attach()

    }
    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.isAuthrized == true){
                    showAuthCard(false)
                    messagesViewModel = ViewModelProvider(this@MessagesFragment)[MessagesViewModel::class.java]
                }else if (it.isAuthrized == null){
                    showAuthCard(false)
                }else{
                    showAuthCard(true)
                }
            }
        }
    }
    fun showAuthCard(v: Boolean){
        if (v){
            binding?.authCard?.visibility = View.VISIBLE
            runTextAnimation()
            binding?.buttonLogin?.setOnClickListener {
                findNavController().navigate(R.id.action_messagesFragment_to_authFragment)
            }
        }else{
            binding?.authCard?.visibility = View.GONE
        }

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