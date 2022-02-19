package com.elt.passsystem.data.datasources.console

interface IConsoleApi {
    fun log(funcName: String, text: String, content: String = "")
}

class ConsoleApi: IConsoleApi {
    override fun log(funcName: String, text: String, content: String) =
        println("********** $funcName: $text - $content".trim())
}