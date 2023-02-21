package uk.co.itmms.androidArchitecture.data

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class BaseDataTest {

    @Before
    fun initMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}