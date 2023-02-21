package uk.co.itmms.androidArchitecture

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class BaseAppAndroidTest {

    @Before
    fun initMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}