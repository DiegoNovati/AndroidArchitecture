package uk.co.itmms.androidArchitecture.data.extensions

import junit.framework.TestCase.assertEquals
import org.junit.Test
import uk.co.itmms.androidArchitecture.data.BaseDataTest
import uk.co.itmms.androidArchitecture.domain.entities.Gender

class ExtensionsGenderTest : BaseDataTest() {

    @Test
    fun `testing toGender`() {
        assertEquals(Gender.Male, "male".toGender())
        assertEquals(Gender.Female, "female".toGender())
        assertEquals(Gender.Female, "".toGender())
    }
}