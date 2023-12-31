package com.dertefter.ficus.view.fragments.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.dertefter.ficus.R
import com.dertefter.ficus.data.news.NewsItem
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.databinding.FragmentNewsBinding
import com.dertefter.ficus.view.adapters.NewsListAdapter
import com.dertefter.ficus.viewmodel.news.NewsViewModel
import com.google.android.material.shape.MaterialShapeDrawable

class NewsFragment : Fragment(R.layout.fragment_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    var isLoadingData = false
    val newsListAdapter: NewsListAdapter = NewsListAdapter(this)
    var columnCount: Int = 1
    fun getNews(){
        newsViewModel.getNews()
    }

    fun readNews(imageView: View, transitionImageName: String, title: String, newsid: String, date: String, imageUrl: String?, color: Int){
        val extras = FragmentNavigatorExtras(
            imageView to transitionImageName,
        )
        val direction: NavDirections =
            NewsFragmentDirections.actionNewsFragmentToReadNewsFragment(
                transitionImageName,
                title,
                imageUrl,
                newsid,
                date,
                color
            )
        findNavController().navigate(direction, extras)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        columnCount = resources.getInteger(R.integer.column_count)

        binding = FragmentNewsBinding.bind(view)
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        observeGetPosts()
        if (binding.newsRecyclerView.adapter?.itemCount == 0) {
            getNews()
        }
        binding.newsTopAppBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(context)


        setupRecyclerView()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupRecyclerView(){
        val recyclerView: RecyclerView = binding.newsRecyclerView
        recyclerView.adapter = newsListAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), columnCount)
        recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            val lastViewedItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            Log.e("scrollingSpeed", lastViewedItemPosition.toString() + " " + newsListAdapter.itemCount)
            if ((lastViewedItemPosition + 1 == newsListAdapter.itemCount && !isLoadingData)) {
                getNews()
            }
        }
        val verticalItemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        verticalItemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        val horizontalItemDecorator = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        horizontalItemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_horizontal)!!)
        if (columnCount > 1){
            recyclerView.addItemDecoration(verticalItemDecorator)
            recyclerView.addItemDecoration(horizontalItemDecorator)
        }else{
            recyclerView.addItemDecoration(verticalItemDecorator)
        }
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        postponeEnterTransition()
    }

    private fun observeGetPosts() {
        newsViewModel.newsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.data)
            }
        }
    }

    private fun onError(error: List<NewsItem>?) {
        newsListAdapter.setNewsList(error)
        isLoadingData = false
    }

    private fun onSuccess(data: List<NewsItem>?) {
        newsListAdapter.setNewsList(data)
        isLoadingData = false
    }

    private fun onLoading() {
        isLoadingData = true
    }


}