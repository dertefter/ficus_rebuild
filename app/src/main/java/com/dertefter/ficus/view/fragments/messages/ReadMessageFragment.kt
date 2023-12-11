package com.dertefter.ficus.view.fragments.messages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.errors.Error
import com.dertefter.ficus.data.news.NewsContent
import com.dertefter.ficus.databinding.FragmentReadMessageBinding
import com.dertefter.ficus.databinding.FragmentReadNewsBinding
import com.dertefter.ficus.utils.ImageGetter
import com.dertefter.ficus.utils.ViewUtils
import com.dertefter.ficus.viewmodel.messages.ReadMessageViewModel
import com.dertefter.ficus.viewmodel.news.ReadNewsViewModel
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.squareup.picasso.Picasso


class ReadMessageFragment : Fragment(R.layout.fragment_read_message) {

    var binding: FragmentReadMessageBinding? = null
    lateinit var readMessageViewModel: ReadMessageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReadMessageBinding.bind(view)
        readMessageViewModel = ViewModelProvider(this)[ReadMessageViewModel::class.java]
        getMessageContent()
        setupAppBar()
        setupFab()
        setupNestedScrollView()

    }

    private fun setupNestedScrollView() {
        binding?.nestedScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 0){
                binding?.scrollUpFab?.show()
            }else{
                binding?.scrollUpFab?.hide()
            }
        }
    }

    private fun showMessageContent(data: String){
        displayHtml(data, binding?.messageContentView!!)
    }

    fun setupFab(){
        binding?.scrollUpFab?.setOnClickListener {
            binding?.nestedScrollView?.smoothScrollTo(0,0)
            binding?.appBarLayout?.setExpanded(true)
        }
        binding?.scrollUpFab?.hide()
    }
    fun setupAppBar(){
        val message = ReadMessageFragmentArgs.fromBundle(requireArguments()).message
        binding?.backButton?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding?.title?.text = message.title
    }

    private fun observeGetPosts() {
        readMessageViewModel.liveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.error)
            }
        }
    }

    private fun onError(error: Error?) {
        ViewUtils().hideView(binding?.progressBar!!)
        ViewUtils().hideView(binding?.nestedScrollView!!)
        binding?.errorView?.visibility = View.VISIBLE
        binding?.retryButton?.setOnClickListener {
            getMessageContent()
        }
    }

    fun getMessageContent(){
        val message = ReadMessageFragmentArgs.fromBundle(requireArguments()).message
        readMessageViewModel.loadMessage(message.mes_id)
        observeGetPosts()
    }

    private fun onSuccess(data: String?) {
        if (data == null){
            onError(null)
            return
        }
        showMessageContent(data)
        ViewUtils().hideView(binding?.progressBar!!)
        ViewUtils().showView(binding?.nestedScrollView!!)
        binding?.errorView?.visibility = View.GONE
    }

    private fun onLoading() {
        ViewUtils().showView(binding?.progressBar!!)
        binding?.errorView?.visibility = View.GONE

    }

    private fun displayHtml(html: String, textView: TextView) {

        // Creating object of ImageGetter class you just created
        val imageGetter = ImageGetter(resources, textView)

        // Using Html framework to parse html
        val styledText= HtmlCompat.fromHtml(html,
            HtmlCompat.FROM_HTML_MODE_COMPACT,
            imageGetter,null)

        // to enable image/link clicking
        textView.movementMethod = LinkMovementMethod.getInstance()

        // setting the text after formatting html and downloading and setting images
        textView.text = styledText
    }

}