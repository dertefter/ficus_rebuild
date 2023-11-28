package com.dertefter.ficus.view.fragments.timetable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.timetable.GroupItem
import com.dertefter.ficus.databinding.FragmentSearchGroupBinding
import com.dertefter.ficus.view.adapters.GroupListAdapter
import com.dertefter.ficus.viewmodel.stateFlow.StateFlowViewModel
import com.dertefter.ficus.viewmodel.timetable.SearchGroupViewModel
import kotlinx.coroutines.launch

class SearchGroupFragment : Fragment(R.layout.fragment_search_group) {
    var binding: FragmentSearchGroupBinding? = null
    lateinit var stateFlowViewModel: StateFlowViewModel
    private lateinit var searchGroupViewModel: SearchGroupViewModel
    val groupListAdapter: GroupListAdapter = GroupListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchGroupBinding.bind(view)
        setupRecyclerView()
        searchGroupViewModel = ViewModelProvider(this)[SearchGroupViewModel::class.java]
        stateFlowViewModel = ViewModelProvider(requireActivity())[StateFlowViewModel::class.java]
        observeGetPosts()
        observeUiState()
        setupToolbar()
        setupSearchbar()
        stateFlowViewModel.checkAuth()
        searchGroupViewModel.getGroups("")
    }

    fun setupRecyclerView(){
        val recyclerView: RecyclerView = binding?.recyclerView!!
        recyclerView.adapter = groupListAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        //val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        //itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        //recyclerView.addItemDecoration(itemDecorator)
    }
    fun setupSearchbar(){
        binding?.searchEditText?.doOnTextChanged { text, start, before, count ->
            searchGroupViewModel.getGroups(text.toString())
        }
    }

    fun setupToolbar(){
        binding?.topAppBar?.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setGroup(group: String){
        searchGroupViewModel.setGroup(group)
        stateFlowViewModel.updateUserData(customGroup = group)
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    fun notifyAction(str: String){
        Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
    }


    private fun observeUiState() {
        lifecycleScope.launch {
            stateFlowViewModel.uiState.collect{
                if (it.isAuthrized == true){
                    groupListAdapter.individualEnabled = true
                }
            }
        }
    }

    private fun observeGetPosts() {
        searchGroupViewModel.liveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
            }
        }
    }



    private fun onError(error: Error?) {
        binding?.progressBar?.visibility = View.GONE
        binding?.recyclerView?.visibility = View.GONE
        Log.e("err", error.toString())
        showError(error)
        groupListAdapter.setGroupList(null)
    }

    fun showError(error: Error?){
        binding?.errorTitle?.text = error?.title
        binding?.errorText?.text = error?.text
        binding?.errorView?.visibility = View.VISIBLE
    }

    private fun onSuccess(data: List<GroupItem>?) {
        groupListAdapter.setGroupList(data)
        binding?.recyclerView?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
        binding?.errorView?.visibility = View.GONE
    }

    private fun onLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.recyclerView?.visibility = View.GONE
        binding?.errorView?.visibility = View.GONE
    }

}