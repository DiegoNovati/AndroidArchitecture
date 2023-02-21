package uk.co.itmms.androidArchitecture.activities

import androidx.navigation.NavHostController
import uk.co.itmms.androidArchitecture.BaseAppTest
import uk.co.itmms.androidArchitecture.services.IServiceNavigation
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class MainViewModelTest : BaseAppTest() {

    @MockK
    private lateinit var mockServiceNavigation: IServiceNavigation

    @MockK
    private lateinit var mockNavHostController: NavHostController

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockServiceNavigation)
    }

    @Test
    fun `testing setNavController`() {
        mainViewModel.setNavController(mockNavHostController)

        verify(exactly = 1) {
            mockServiceNavigation.setNavController(mockNavHostController)
        }
        confirmVerified(mockServiceNavigation, mockServiceNavigation)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `testing backButtonPressed`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        mainViewModel.backButtonPressed()

        verify(exactly = 1) {
            mockServiceNavigation.updateCurrentRoute()
        }
        confirmVerified(mockServiceNavigation, mockServiceNavigation)
    }
}