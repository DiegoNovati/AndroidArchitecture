package uk.co.itmms.androidArchitecture.domain

import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentAnalytics
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryDevelopmentLogger
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

abstract class BaseDomainTest {

    @MockK
    protected lateinit var mockRepositoryDevelopmentLogger: IRepositoryDevelopmentLogger

    @MockK
    protected lateinit var mockRepositoryDevelopmentAnalytics: IRepositoryDevelopmentAnalytics

    @Before
    fun initMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}