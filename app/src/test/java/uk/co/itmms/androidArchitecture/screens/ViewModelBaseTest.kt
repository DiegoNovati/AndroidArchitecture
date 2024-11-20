package uk.co.itmms.androidArchitecture.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import uk.co.itmms.androidArchitecture.services.IServiceNavigation

class ViewModelBaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `testing initial state`() {
        val initialState = "initial state"

        val viewModelBase = MyViewModel(
            initialState = initialState,
        )

        assertEquals(initialState, viewModelBase.state.value)
    }

    @Test
    fun `testing state update`() {
        val newState = "new state"

        val viewModelBase = MyViewModel(
            initialState = "initial state",
        )

        viewModelBase.updateState(newState)

        assertEquals(newState, viewModelBase.state.value)
    }

    private class MyViewModel(
        initialState: String,
    ): ViewModelBase<String>(
        serviceNavigation = mockk<IServiceNavigation>(),
        initialState = initialState,
    ) {
        fun updateState(newState: String) {
            localState = newState
        }
    }
}