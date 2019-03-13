package com.example.homestay.utils

import com.example.homestay.model.User

object StoreCurrentUserInfo {
    private lateinit var mUser: User

    fun setUser(user: User){
        this.mUser = user
    }

    fun getUser(): User{
        return this.mUser
    }
}