package uk.co.itmms.androidArchitecture.domain.usecases.home

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import uk.co.itmms.androidArchitecture.domain.BaseDomainTest
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.repositories.IRepositorySession
import uk.co.itmms.androidArchitecture.domain.usecases.NoParams

class UseCaseHomeInitTest : BaseDomainTest() {

    @MockK
    private lateinit var mockRepositorySession: IRepositorySession

    @InjectMockKs
    private lateinit var useCaseHomeInit: UseCaseHomeInit

    @After
    fun tearDown() {
        confirmVerified(mockRepositorySession)
    }

    @Test
    fun `testing success`() = runBlocking {
        val mockCustomerList: List<Customer> = mockk()
        val mockBookingList: List<Booking> = mockk()

        coEvery { mockRepositorySession.customerList } returns mockCustomerList
        coEvery { mockRepositorySession.bookingList } returns mockBookingList

        val actual = useCaseHomeInit.run(NoParams)

        actual.fold({
            fail("Unexpected failure")
        }){ result ->
            assertEquals(mockCustomerList, result.customerList)
            assertEquals(mockBookingList, result.bookingList)
        }

        coVerify(exactly = 1) {
            mockRepositorySession.customerList
            mockRepositorySession.bookingList
        }
    }
}