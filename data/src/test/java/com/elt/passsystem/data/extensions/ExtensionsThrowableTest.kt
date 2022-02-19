package com.elt.passsystem.data.extensions

import junit.framework.TestCase.assertTrue
import org.junit.Test

class ExtensionsThrowableTest {

    @Test
    fun testFullStackTraceToString() {
        val message = "exception message"
        val t = RuntimeException(message)

        val actual = t.fullStackTraceToString()

        assertTrue(actual.contains(message))
        assertTrue(actual.contains("RuntimeException"))
    }
}