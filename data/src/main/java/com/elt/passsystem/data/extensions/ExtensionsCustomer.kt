package com.elt.passsystem.data.extensions

import com.elt.passsystem.data.models.DBCustomer
import com.elt.passsystem.data.models.NetCustomer
import com.elt.passsystem.domain.entities.Customer

fun DBCustomer.toCustomer(): Customer =
    Customer(
        customerBid = this.bid,
        name = this.name,
        address = this.location ?: "",
    )

fun List<DBCustomer>.toCustomerList(): List<Customer> =
    this.map { it.toCustomer() }

fun NetCustomer.toDBCustomer(): DBCustomer =
    DBCustomer(
        bid = this.bid,
        uuid = this.uuid,
        title = this.title,
        firstname = this.firstname,
        location = this.location,
    )

fun List<NetCustomer>.toDBCustomerList(): List<DBCustomer> =
    this.map { it.toDBCustomer() }