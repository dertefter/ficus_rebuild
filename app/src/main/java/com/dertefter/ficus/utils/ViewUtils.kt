package com.dertefter.ficus.utils

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import com.dertefter.ficus.R

class ViewUtils {

    private val animationDuration: Long = R.integer.animation_duration.toLong()
    fun showView(view: View) {
        ObjectAnimator.ofFloat(view, "alpha",  1f).start()
    }

    fun hideView(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
    }

}