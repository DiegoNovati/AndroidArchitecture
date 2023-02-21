package uk.co.itmms.androidArchitecture.data.datasources.logging

import android.util.Log

interface ILoggingConsole {
    enum class MessageType {
        Debug, Info, Warn, Error
    }

    suspend fun write(messageType: MessageType, tag: String? = null, message: String)
}

class LoggingConsole : ILoggingConsole {
    override suspend fun write(
        messageType: ILoggingConsole.MessageType,
        tag: String?,
        message: String
    ) {
        when (messageType) {
            ILoggingConsole.MessageType.Debug -> Log.d(tag, message)
            ILoggingConsole.MessageType.Info -> Log.i(tag, message)
            ILoggingConsole.MessageType.Warn -> Log.w(tag, message)
            ILoggingConsole.MessageType.Error -> Log.e(tag, message)
        }
    }
}