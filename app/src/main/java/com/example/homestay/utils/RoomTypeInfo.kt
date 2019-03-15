package com.example.homestay.utils

import java.util.ArrayList

object RoomTypeInfo {

    fun getListImageSingleRoom(): ArrayList<String> {
        var listImageSingleRoom: ArrayList<String> = ArrayList()

        listImageSingleRoom.add("https://hotelpresident.hu/application/files/cache/thumbnails/237548576125a688705f0e44b520c324.jpg")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR91Ui-vuuhIiEpdTEez4YvHtekHYxWqLQDo-Y8wx_JPzFH3KSo")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAvm3FFO7oTJgXkTFo7dsf6FPfdsmy0HcyLLVpYrDE-D1LM6KZ")
        listImageSingleRoom.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkcKiFife8NWR33T2BV6J8-UMO-GewK6BG4MNBrf-Ei6RhaVXt")
        return listImageSingleRoom
    }

    fun getListImageDoubleRoom(): ArrayList<String> {
        var listImageDoubleRoom: ArrayList<String> = ArrayList()

        listImageDoubleRoom.add("https://media-cdn.tripadvisor.com/media/photo-s/01/fb/17/35/twin-bedroom.jpg")
        listImageDoubleRoom.add("http://cntravelcambodia.com/filelibrary/2492016147467083974567.jpg")
        listImageDoubleRoom.add("https://www.granviakyoto.com/rooms/assets_c/2014/03/std_tw06-thumb-960xauto-33.jpg")
        listImageDoubleRoom.add("http://hotelhennessis.com/wp-content/uploads/2015/12/Hennessis-Hotel-Twin-Room-Setting.jpg")
        return listImageDoubleRoom
    }

    fun getListImageFamilyRoom(): ArrayList<String> {
        var listImageFamilyRoom: ArrayList<String> = ArrayList()

        listImageFamilyRoom.add("https://www.regalhotel.com/uploads/roh/accommodations/720x475/ROH_RM629-Family-Room-Quadruple.jpg")
        listImageFamilyRoom.add("https://pix10.agoda.net/hotelImages/565/5651/5651_17080313560054867934.jpg?s=1024x768")
        listImageFamilyRoom.add("https://media-cdn.tripadvisor.com/media/photo-s/0b/05/9e/d2/family-quadruple-room.jpg")
        listImageFamilyRoom.add("https://www.regalhotel.com/uploads/rah/promotion/room/720x475/RAH-FamilyQuadruple2.jpg")
        return listImageFamilyRoom
    }
}