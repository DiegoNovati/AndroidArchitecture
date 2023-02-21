package uk.co.itmms.androidArchitecture.extensions

import uk.co.itmms.androidArchitecture.domain.entities.BookingStatus

fun BookingStatus.toDescription(): String =
    this.javaClass.simpleName