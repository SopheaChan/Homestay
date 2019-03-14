package com.example.homestay.utils

import com.example.homestay.model.BookingInfo
import com.example.homestay.model.FavoriteList
import com.example.homestay.model.User

object StoreCurrentUserInfo {
    private lateinit var mUser: User
    private lateinit var mBookingRecordList: ArrayList<BookingInfo>
    private lateinit var mFavoriteList: ArrayList<FavoriteList?>

    fun setUser(user: User){
        this.mUser = user
    }

    fun getUser(): User{
        return this.mUser
    }

    fun setBookingInfo(bookingRecord: ArrayList<BookingInfo>){
        this.mBookingRecordList = bookingRecord
    }

    fun getBookingRecordList(): ArrayList<BookingInfo>{
        return this.mBookingRecordList
    }

    fun setFavoriteList(favoriteList: ArrayList<FavoriteList?>){
        this.mFavoriteList = favoriteList
    }

    fun getFavoriteList():  ArrayList<FavoriteList?>{
        return this.mFavoriteList
    }
}