package uk.co.itmms.specifications.test

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import cucumber.api.CucumberOptions
import cucumber.api.SnippetType
import cucumber.api.android.CucumberInstrumentationCore

@CucumberOptions(
    features = [
        "features"
    ],
    glue = [
        "uk/co/itmms/specifications/steps"
    ],
    plugin = [],
    tags = [],
    snippets = SnippetType.CAMELCASE
)
class CucumberTestRunner : AndroidJUnitRunner() {
    private val instrumentationCore: CucumberInstrumentationCore = CucumberInstrumentationCore(this)

    override fun onCreate(bundle: Bundle) {
        val tags: String = BuildConfig.TEST_TAGS
        if (tags.isNotEmpty()) {
            bundle.putString("tags", tags.replace("\\s".toRegex(), ""))
        }
        var scenario: String = BuildConfig.TEST_SCENARIO
        if (scenario.isNotEmpty()) {
            scenario = scenario.replace(" ".toRegex(), "\\\\s")
            bundle.putString("name", scenario)
        }
        instrumentationCore.create(bundle)
        super.onCreate(bundle)
    }

    override fun onStart() {
        waitForIdleSync()
        instrumentationCore.start()
    }
}