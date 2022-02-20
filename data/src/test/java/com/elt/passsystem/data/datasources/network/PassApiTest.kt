package com.elt.passsystem.data.datasources.network

import com.elt.passsystem.data.BaseDataRobolectricTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import org.junit.Before
import org.junit.Test

class PassApiTest: BaseDataRobolectricTest() {

    private val baseUrl = "https://qaappapi.passgenius.com"
    private val username = "DavidB811543"
//    private val username = "PhilF125314"
    private val password = "Qwaszx12"

    private lateinit var passApi: IPassApi

    @Before
    fun setUp() {
        passApi = createPassApi(context, releaseMode = false)
    }

    @Test
    fun `integration tests`() = runBlocking<Unit>(Dispatchers.IO) {
        val auth = Credentials.basic(username, password)
        val authenticateResponse = passApi.authenticate(auth = auth)
        val authorization = "Bearer ${authenticateResponse.token}"
        val officeBid = authenticateResponse.offices[0].bid

        passApi.getCustomerList(authorization, officeBid)

        passApi.getBookingList(authorization, officeBid)

        println("auth = $auth")
        println(authenticateResponse)
    }
}