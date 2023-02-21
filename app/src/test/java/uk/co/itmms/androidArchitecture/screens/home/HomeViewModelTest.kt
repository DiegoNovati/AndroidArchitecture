package uk.co.itmms.androidArchitecture.screens.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import arrow.core.right
import uk.co.itmms.androidArchitecture.BaseAppTest
import uk.co.itmms.androidArchitecture.domain.entities.Booking
import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import uk.co.itmms.androidArchitecture.domain.entities.Customer
import uk.co.itmms.androidArchitecture.domain.failures.FailureHome
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeInit
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeLogout
import uk.co.itmms.androidArchitecture.domain.usecases.home.UseCaseHomeMonitor
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseAppTest() {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var mockServiceNavigation: IServiceNavigation

    @MockK
    private lateinit var mockUseCaseHomeInit: UseCaseHomeInit

    @MockK
    private lateinit var mockUseCaseHomeMonitor: UseCaseHomeMonitor

    @MockK
    private lateinit var mockUseCaseHomeLogout: UseCaseHomeLogout

    private lateinit var homeViewModel: HomeViewModel

    private val customerList = listOf(
        createCustomer(1),
        createCustomer(2),
        createCustomer(3),
    )

    private val bookingList = listOf(
        createBooking(1, BookingStatus.Completed),
        createBooking(2, BookingStatus.Started),
        createBooking(3, BookingStatus.Scheduled),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)  // Required by 'viewModelScope.launch'

        // Required by init
        val useCaseHomeInitResult: Either<FailureHome, UseCaseHomeInit.Result> =
            UseCaseHomeInit.Result(
                customerList = customerList,
                bookingList = bookingList,
            ).right()
        every { mockUseCaseHomeInit.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureHome, UseCaseHomeInit.Result>) -> Unit>().invoke(useCaseHomeInitResult)
            Job()
        }

        val useCaseHomeMonitorResult: Either<FailureHome, UseCaseHomeMonitor.Result> =
            UseCaseHomeMonitor.Result(connected = flow {}).right()
        every { mockUseCaseHomeMonitor.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureHome, UseCaseHomeMonitor.Result>) -> Unit>().invoke(useCaseHomeMonitorResult)
            Job()
        }

        homeViewModel = HomeViewModel(
            mockServiceNavigation, mockUseCaseHomeInit, mockUseCaseHomeMonitor, mockUseCaseHomeLogout,
        )
    }

    @Test
    fun `testing UseCaseHomeInit when returning successfully`() {
        val actual = homeViewModel.state.value

        assertEquals(customerList, actual?.data?.customerList)
        assertEquals(bookingList, actual?.data?.bookingList)
    }

    @Test
    fun `testing that any change of connection value updates the state`() {
        val connected = false

        val result: Either<FailureHome, UseCaseHomeMonitor.Result> =
            UseCaseHomeMonitor.Result(connected = flow { emit(connected) }).right()
        every { mockUseCaseHomeMonitor.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureHome, UseCaseHomeMonitor.Result>) -> Unit>().invoke(result)
            Job()
        }

        homeViewModel = HomeViewModel(
            mockServiceNavigation, mockUseCaseHomeInit, mockUseCaseHomeMonitor, mockUseCaseHomeLogout,
        )

        val loginState = homeViewModel.state.value
        assertEquals(connected, loginState?.data?.connected)

        verify(exactly = 2) {
            mockUseCaseHomeInit.invoke(any(), any(), any())
            mockUseCaseHomeMonitor.invoke(any(), any(), any())
        }
        confirmVerified(mockServiceNavigation, mockUseCaseHomeInit, mockUseCaseHomeMonitor, mockUseCaseHomeLogout)
    }

    @Test
    fun `testing doEvent when eventType is Logout`() {
        val result: Either<FailureHome, Unit> = Unit.right()
        every { mockUseCaseHomeLogout.invoke(any(), any(), any()) } answers {
            thirdArg<(Either<FailureHome, Unit>) -> Unit>().invoke(result)
            Job()
        }

        homeViewModel.doEvent(HomeViewModel.EventType.Logout)

        verify(exactly = 1) {
            mockUseCaseHomeInit.invoke(any(), any(), any())
            mockUseCaseHomeMonitor.invoke(any(), any(), any())
            mockUseCaseHomeLogout.invoke(any(), any(), any())
            mockServiceNavigation.popBack()
        }
        confirmVerified(mockServiceNavigation, mockUseCaseHomeInit, mockUseCaseHomeMonitor, mockUseCaseHomeLogout)
    }

    private fun createCustomer(id: Int): Customer =
        Customer(
            customerBid = "$id",
            name = "Name $id",
            address = "Address $id",
        )

    private fun createBooking(id: Int, status: BookingStatus): Booking =
        Booking(
            bookingBid = id.toLong(),
            customerBid = "$id",
            status = status,
            start = Date(),
            end = Date()
        )
}