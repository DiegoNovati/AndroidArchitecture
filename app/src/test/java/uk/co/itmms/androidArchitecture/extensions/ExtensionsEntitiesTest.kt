package uk.co.itmms.androidArchitecture.extensions

import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ExtensionsEntitiesTest {

    @Test
    fun `testing toDescription()`() {
        assertEquals("Scheduled", BookingStatus.Scheduled.toDescription())
        assertEquals("Started", BookingStatus.Started.toDescription())
        assertEquals("Completed", BookingStatus.Completed.toDescription())
    }
}