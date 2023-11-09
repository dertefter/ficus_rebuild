package com.dertefter.ficus

import android.app.Application
import com.dertefter.ficus.repositoty.local.AppPreferences
import com.google.android.material.color.DynamicColors

class Ficus: Application(){
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        AppPreferences.setup(applicationContext)
    }
}