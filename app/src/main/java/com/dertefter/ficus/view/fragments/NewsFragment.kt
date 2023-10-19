package com.dertefter.ficus.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.databinding.FragmentNewsBinding
import com.dertefter.ficus.view.adapters.NewsListAdapter
import com.dertefter.ficus.viewmodel.NewsViewModel
import com.google.android.material.shape.MaterialShapeDrawable


class NewsFragment : Fragment(R.layout.fragment_news) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        setupRecyclerView()
        observeGetPosts()
        if (savedInstanceState == null) {
            newsViewModel.getNews(1)
        }
    }

    fun setupRecyclerView(){
        val recyclerView: RecyclerView = binding.newsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        recyclerView.addItemDecoration(itemDecorator)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun observeGetPosts() {
        newsViewModel.newsLiveData.observe(viewLifecycleOwner, Observer {
            Log.e("status", it.status.toString())
            binding.newsRecyclerView.adapter = NewsListAdapter(it.data)
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
                else -> {}
            }
        })
    }

    private fun onError(error: Any?) {
        binding.newsProgressBar.visibility = View.INVISIBLE
    }

    private fun onSuccess(data: Any?) {
        binding.newsProgressBar.visibility = View.INVISIBLE
        Log.e("TAG", data.toString())
    }

    private fun onLoading() {
        binding.newsProgressBar.visibility = View.VISIBLE
    }

}