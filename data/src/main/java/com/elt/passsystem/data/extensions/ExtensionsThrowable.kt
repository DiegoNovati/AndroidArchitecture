package com.elt.passsystem.data.extensions

import java.io.PrintWriter
import java.io.StringWriter


fun Throwable.fullStackTraceToString(): String {
    StringWriter().use { stringWriter ->
        PrintWriter(stringWriter).use { printWriter ->
            this.printStackTrace(printWriter)
            return stringWriter.toString()
        }
    }
}