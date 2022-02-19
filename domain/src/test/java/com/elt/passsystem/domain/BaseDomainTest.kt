package com.elt.passsystem.domain

import io.mockk.MockKAnnotations
import org.junit.Before

abstract class BaseDomainTest {

    @Before
    fun initMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}