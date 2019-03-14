package com.example.homestay.model

data class FavoriteList(var hotelName: String ?= null, var address: String ?= null, var issueDate: String ?= null,
                        var imageUrl: String ?= null){
    constructor(): this(null, null, null, null)
}