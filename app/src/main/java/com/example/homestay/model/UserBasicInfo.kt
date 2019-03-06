package com.example.homestay.model

import java.io.Serializable

data class UserBasicInfo(var name: String ?= null, var sex: String ?= null, var age: String ?= null, var address: String ?= null): Serializable{
    constructor(): this(null, null, null, null)
}