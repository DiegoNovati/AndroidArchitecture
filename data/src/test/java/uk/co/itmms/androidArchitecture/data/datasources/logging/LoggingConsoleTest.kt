package uk.co.itmms.androidArchitecture.data.datasources.logging

import uk.co.itmms.androidArchitecture.data.BaseDataRobolectricTest
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoggingConsoleTest : BaseDataRobolectricTest() {

    private lateinit var loggingConsole: LoggingConsole

    @Before
    fun setUp() {
        loggingConsole = LoggingConsole()
    }

    @Test
    fun `testing write`() = runBlocking {
        try {
            loggingConsole.write(ILoggingConsole.MessageType.Error, null, "error message")
            loggingConsole.write(ILoggingConsole.MessageType.Warn, "tag","warning message")
            loggingConsole.write(ILoggingConsole.MessageType.Info, null,"info message")
            loggingConsole.write(ILoggingConsole.MessageType.Debug, "tag","debug message")
        } catch (e: Throwable) {
            fail("The call to method write should neve raise an exception")
        }
    }
}