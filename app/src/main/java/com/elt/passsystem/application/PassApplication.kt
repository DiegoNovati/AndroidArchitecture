package com.elt.passsystem.application

import androidx.multidex.MultiDexApplication
import com.elt.passsystem.BuildConfig
import com.elt.passsystem.data.DataInterface
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PassApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        DataInterface.initDataLayer(this, BuildConfig.BUILD_TYPE == "release")
    }
}