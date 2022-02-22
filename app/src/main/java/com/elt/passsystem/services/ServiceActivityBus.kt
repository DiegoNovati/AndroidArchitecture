package com.elt.passsystem.services

import javax.inject.Inject

interface IServiceActivityBus {
    suspend fun notifyBackButtonPressed()
    fun registerBackButtonPressedListener(listener: () -> Unit)
    fun unregisterBackButtonPressedListener()
}

class ServiceActivityBus @Inject constructor(
): IServiceActivityBus {
    private var backButtonPressedListener: (() -> Unit)? = null

    override suspend fun notifyBackButtonPressed() {
        this.backButtonPressedListener?.invoke()
    }

    override fun registerBackButtonPressedListener(listener: () -> Unit) {
        this.backButtonPressedListener = listener
    }

    override fun unregisterBackButtonPressedListener() {
        this.backButtonPressedListener = null
    }
}