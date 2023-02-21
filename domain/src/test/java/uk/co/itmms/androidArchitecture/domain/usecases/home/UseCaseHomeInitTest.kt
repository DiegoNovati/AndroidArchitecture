package uk.co.itmms.androidArchitecture.domain.usecases.home

import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositoryRuntime
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UseCaseHomeInitTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositoryRuntime: IRepositoryRuntime

    private lateinit var useCaseHomeInit: UseCaseHomeInit

    @Before
    fun setUp() {
        useCaseHomeInit = UseCaseHomeInit(
            mockRepositoryDevelopmentLogger, mockRepositoryDevelopmentAnalytics, mockRepositoryRuntime
        )
    }

    @Test
    fun `testing success`() = runBlocking {
        val mockCustomerList: List<Customer> = mockk()
        val mockBookingList: List<Booking> = mockk()

        coEvery { mockRepositoryRuntime.getCustomerList() } returns mockCustomerList
        coEvery { mockRepositoryRuntime.getBookingList() } returns mockBookingList

        val actual = useCaseHomeInit.run(NoParams)

        actual.fold({
            fail("Unexpected failure")
        }){ result ->
            assertEquals(mockCustomerList, result.customerList)
            assertEquals(mockBookingList, result.bookingList)
        }

        coVerify(exactly = 1) {
            mockRepositoryRuntime.getCustomerList()
            mockRepositoryRuntime.getBookingList()
        }
        confirmVerified(mockRepositoryRuntime)
    }
}