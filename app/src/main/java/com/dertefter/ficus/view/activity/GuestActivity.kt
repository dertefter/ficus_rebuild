package com.dertefter.ficus.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.ActivityGuestBinding
import com.dertefter.ficus.view.fragments.auth.AuthFragment
import com.dertefter.ficus.view.fragments.news.ReadNewsFragment
import com.dertefter.ficus.view.fragments.timetable.SearchGroupFragment
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestActivity : AppCompatActivity() {

    private lateinit var stateFlowViewModel: StateFlowViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ActivityGuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityGuestBinding.inflate(layoutInflater)

        stateFlowViewModel = ViewModelProvider(this)[StateFlowViewModel::class.java]
        setContentView(binding.root)
        setupBottomNavigation()
        observeUiState()
        stateFlowViewModel.checkAuth()

    }
    fun hideBottomNavigation(v: Boolean){
        CoroutineScope(Dispatchers.Main).launch {
            if (v) {
                binding.bottomNavigation?.visibility = View.GONE
                binding.navigationRail?.visibility = View.GONE
            }else{
                binding.bottomNavigation?.visibility = View.VISIBLE
                binding.navigationRail?.visibility = View.VISIBLE
            }
        }
    }

    private fun observeUiState() {
       lifecycleScope.launch {
           stateFlowViewModel.uiState.collect{
               if (it.isAuthrized == true){
                   stateFlowViewModel.updateUserData()
                   stateFlowViewModel.updateTimetableData()
               }else{
                   stateFlowViewModel.updateTimetableData()
               }
           }
       }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun checkIsFragmentHideBottomNavigation(id: Int?){
        val hideFragments = listOf<Int>(
            R.id.readNewsFragment,
            R.id.authFragment,
            R.id.setGroupFragment,
            R.id.readMessagesFragment,
            R.id.readMessageFragment
        )
        if (id != null) {
            if (hideFragments.contains(id)){
                hideBottomNavigation(true)
            }else{
                hideBottomNavigation(false)
            }
        }
    }

    private fun setupBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        checkIsFragmentHideBottomNavigation(navController.currentDestination?.id)
        binding.bottomNavigation?.setupWithNavController(navController)
        binding.navigationRail?.setupWithNavController(navController)


        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {


            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                TransitionManager.beginDelayedTransition(binding.root, Fade().excludeTarget(
                    R.id.nav_host_fragment, false))
                checkIsFragmentHideBottomNavigation(navController.currentDestination?.id)
            }

        }, true)


    }

}