package com.dertefter.ficus.view.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dertefter.ficus.R
import com.dertefter.ficus.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    var binding: FragmentProfileBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding?.profileTopAppBar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile_settings -> {
                    val dialogFragment = ProfileDialogFragment()
                    dialogFragment.show(childFragmentManager, "My  Fragment")
                    true
                }
                else -> false
            }
        }
    }
}