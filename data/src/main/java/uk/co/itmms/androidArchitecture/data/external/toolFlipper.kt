package uk.co.itmms.androidArchitecture.data.external

import android.content.Context
import uk.co.itmms.androidArchitecture.data.BuildConfig
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader

fun initFlipper(applicationContext: Context, networkFlipperPlugin: NetworkFlipperPlugin) {
    if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(applicationContext)) {

        SoLoader.init(applicationContext, false)

        with(AndroidFlipperClient.getInstance(applicationContext)) {
            addPlugin(CrashReporterPlugin.getInstance())
            addPlugin(DatabasesFlipperPlugin(applicationContext))
            addPlugin(
                InspectorFlipperPlugin(
                    applicationContext,
                    DescriptorMapping.withDefaults()
                )
            )
            addPlugin(networkFlipperPlugin)
            addPlugin(LeakCanary2FlipperPlugin())

            start()
        }
    }
}