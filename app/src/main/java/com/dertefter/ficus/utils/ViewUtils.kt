package com.dertefter.ficus.utils

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.palette.graphics.Palette
import com.dertefter.ficus.R

class ViewUtils {

    private val animationDuration: Long = R.integer.animation_duration.toLong()
    fun showView(view: View) {
        ObjectAnimator.ofFloat(view, "alpha",  1f).start()
    }

    fun hideView(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
    }

    fun getDominantColor(bitmap: Bitmap): Int {
        return Palette.from(bitmap).generate().getVibrantColor(
            Color.GRAY
        )

    }

}