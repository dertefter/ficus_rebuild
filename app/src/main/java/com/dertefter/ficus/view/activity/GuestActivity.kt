package com.dertefter.ficus.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.ActivityMainBinding
import com.dertefter.ficus.view.fragments.news.ReadNewsFragment
import com.dertefter.ficus.view.fragments.timetable.SearchGroupFragment

class GuestActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation(binding)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
    private fun setupBottomNavigation(binding: ActivityMainBinding){
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                TransitionManager.beginDelayedTransition(binding.root, Fade().excludeTarget(
                    R.id.nav_host_fragment, false))
                when (f) {
                    is ReadNewsFragment -> {
                        binding.bottomNavigation.visibility = View.GONE
                    }
                    is SearchGroupFragment -> {
                        binding.bottomNavigation.visibility = View.GONE
                    }

                    else -> {
                        binding.bottomNavigation.visibility = View.VISIBLE
                    }
                }
            }
        }, true)

    }

}