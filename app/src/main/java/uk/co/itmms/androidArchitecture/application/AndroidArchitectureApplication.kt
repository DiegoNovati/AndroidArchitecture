package uk.co.itmms.androidArchitecture.application

import androidx.multidex.MultiDexApplication
import uk.co.itmms.androidArchitecture.BuildConfig
import uk.co.itmms.androidArchitecture.data.DataInterface
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AndroidArchitectureApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        DataInterface.initDataLayer(this, BuildConfig.BUILD_TYPE == "release")
    }
}