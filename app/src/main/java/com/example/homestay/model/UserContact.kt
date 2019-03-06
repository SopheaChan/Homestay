package com.example.homestay.model

import java.io.Serializable

data class UserContact(var phone: String ?= null, var email: String ?= null, var otherContact: String ?= null): Serializable{
    constructor() : this(null, null, null)
}