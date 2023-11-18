package com.dertefter.ficus.view.fragments.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.dertefter.ficus.R
import com.dertefter.ficus.data.Status
import com.dertefter.ficus.data.news.NewsContent
import com.dertefter.ficus.databinding.FragmentReadNewsBinding
import com.dertefter.ficus.utils.ImageGetter
import com.dertefter.ficus.utils.ViewUtils
import com.dertefter.ficus.viewmodel.ReadNewsViewModel
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.squareup.picasso.Picasso


class ReadNewsFragment : Fragment() {

    var binding: FragmentReadNewsBinding? = null
    private lateinit var transitionImageName: String
    private lateinit var transitionTitleName: String
    private lateinit var readNewsViewModel: ReadNewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var color = ReadNewsFragmentArgs.fromBundle(requireArguments()).color
        val context: Context = DynamicColors.wrapContextIfAvailable(
            requireContext(),
            DynamicColorsOptions.Builder()
                .setContentBasedSource(color)
                .build()
        )

        return layoutInflater.cloneInContext(context).inflate(R.layout.fragment_read_news, container, false)
    }
    fun getNews(newsid: String){
        readNewsViewModel.getNewsMore(newsid)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReadNewsBinding.bind(view)

        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        transitionImageName = ReadNewsFragmentArgs.fromBundle(requireArguments()).transitionImageName
        val newsid = ReadNewsFragmentArgs.fromBundle(requireArguments()).newsid


        readNewsViewModel = ViewModelProvider(this)[ReadNewsViewModel::class.java]
        observeGetPosts()
        getNews(newsid)

        setupAppBar()
        setupFab()
        setupNestedScrollView()
        setupTransitionNames()

    }


    private fun setupTransitionNames() {
        binding?.root?.transitionName = transitionImageName
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

    private fun showNewsContent(data: NewsContent){
        displayHtml(data.contentHtml, binding?.newsContentView!!)
        displayHtml(data.contactsHtml, binding?.newsContactsView!!)
    }

    fun setupFab(){
        binding?.scrollUpFab?.setOnClickListener {
            binding?.nestedScrollView?.smoothScrollTo(0,0)
            binding?.appBarLayout?.setExpanded(true)
        }
        binding?.scrollUpFab?.hide()
    }
    fun setupAppBar(){
        val imageUrl = ReadNewsFragmentArgs.fromBundle(requireArguments()).imageUrl
        if (imageUrl != null){
            Picasso.get().load(imageUrl).resize(600,400).centerCrop().into(binding?.backgroundNews)
        }else{
            binding?.backgroundNews?.visibility = View.GONE
        }
        binding?.backButton?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding?.shareButton?.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://nstu.ru/news/news_more?idnews=${ReadNewsFragmentArgs.fromBundle(requireArguments()).newsid}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        binding?.title?.text = ReadNewsFragmentArgs.fromBundle(requireArguments()).title
    }

    private fun observeGetPosts() {
        readNewsViewModel.newsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> onLoading()
                Status.SUCCESS -> onSuccess(it.data)
                Status.ERROR -> onError(it.data)
            }
        }
    }

    private fun onError(error: NewsContent?) {
        ViewUtils().hideView(binding?.progressBar!!)
        ViewUtils().hideView(binding?.nestedScrollView!!)
        binding?.errorView?.visibility = View.VISIBLE
        binding?.retryButton?.setOnClickListener {
            getNews(ReadNewsFragmentArgs.fromBundle(requireArguments()).newsid)
        }
    }

    private fun onSuccess(data: NewsContent?) {
        if (data == null){
            onError(null)
            return
        }
        showNewsContent(data)
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