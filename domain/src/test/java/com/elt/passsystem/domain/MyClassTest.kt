package com.elt.passsystem.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MyClassTest {

    @Test
    fun `testing that JUnit works as expected`() {
        val actual = MyClass().doSomething()

        assertNotNull(actual)
        assertEquals("Testing JUnit configuration. Does it work ?", actual)
    }
}