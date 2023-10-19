package com.dertefter.ficus

import android.app.Application
import com.google.android.material.color.DynamicColors

class Ficus: Application(){
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}