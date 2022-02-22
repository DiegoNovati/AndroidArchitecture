package com.elt.passsystem.extensions

import com.elt.passsystem.domain.entities.BookingStatus

fun BookingStatus.toDescription(): String =
    this.javaClass.simpleName