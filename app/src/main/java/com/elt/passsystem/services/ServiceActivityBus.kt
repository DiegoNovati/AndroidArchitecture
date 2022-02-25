package com.elt.passsystem.services

import javax.inject.Inject

typealias ConnectivityStateListener = (Boolean) -> Unit

interface IServiceActivityBus {
    suspend fun notifyBackButtonPressed()
    fun registerBackButtonPressedListener(listener: () -> Unit)
    fun unregisterBackButtonPressedListener()

    suspend fun notifyConnectivityStateChanged(connected: Boolean)
    fun getConnectivityState(): Boolean
    fun registerConnectivityStateChanged(listener: ConnectivityStateListener)
    fun unregisterConnectivityStateChanged(listener: ConnectivityStateListener)
}

class ServiceActivityBus @Inject constructor(
): IServiceActivityBus {
    private var backButtonPressedListener: (() -> Unit)? = null
    private val connectivityStateChangedListener = mutableListOf<ConnectivityStateListener>()
    private var connected = true

    override suspend fun notifyBackButtonPressed() {
        this.backButtonPressedListener?.invoke()
    }

    override fun registerBackButtonPressedListener(listener: () -> Unit) {
        this.backButtonPressedListener = listener
    }

    override fun unregisterBackButtonPressedListener() {
        this.backButtonPressedListener = null
    }

    override suspend fun notifyConnectivityStateChanged(connected: Boolean) {
        this.connected = connected
        connectivityStateChangedListener.forEach {
            try {
                it.invoke(connected)
            } catch (e: Throwable) { }
        }
    }

    override fun getConnectivityState(): Boolean =
        connected

    override fun registerConnectivityStateChanged(listener: ConnectivityStateListener) {
        connectivityStateChangedListener.add(listener)
    }

    override fun unregisterConnectivityStateChanged(listener: ConnectivityStateListener) {
        connectivityStateChangedListener.remove(listener)
    }
}