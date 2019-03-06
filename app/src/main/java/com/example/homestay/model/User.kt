package com.example.homestay.model

import java.io.Serializable

class User(var userBasicInfo: UserBasicInfo ?= null, var userContact: UserContact ?= null, var uProfile: String ?= null) : Serializable{
    constructor(): this(null, null, null)
}