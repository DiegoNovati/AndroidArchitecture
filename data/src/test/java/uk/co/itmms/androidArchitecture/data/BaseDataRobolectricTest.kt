package uk.co.itmms.androidArchitecture.data

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(
    // We are running the tests on the minSdk and the targetSdk
    sdk = [Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.S],
    application = RobolectricApplication::class
)
@RunWith(RobolectricTestRunner::class)
abstract class BaseDataRobolectricTest {

    lateinit var context: Context

    @Before
    fun initApplication() {
        val activityController = Robolectric.buildActivity(RobolectricActivity::class.java).create().start().resume()
        context = activityController.get()
    }

    private class RobolectricActivity: Activity()
}

private class RobolectricApplication: Application()