package com.example.homestay.model

import java.io.Serializable

data class BookingInfo(
    var checkInDate: String? = null,
    var checkOutDate: String? = null,
    var numbOfDays: Int? = null,
    var roomType: String ?= null,
    var numbOfGuest: Int ?= null,
    var roomQty: Int ?= null,
    var guestName: String? = null,
    var totalAmount: String? = null,
    var prepaidAmount: String? = null,
    var checkInTime: String? = null,
    var checkOutTime: String ?= null,
    var hotelName: String ?= null,
    var hotelAddress: String ?= null,
    val issuedDate: String ?= null
) : Serializable {
    constructor() : this(
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    )
}
