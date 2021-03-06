package com.example.homestay.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.model.HotelData


open class HomeAdapter(
    private val context: Context, private var listHotel: MutableList<HotelData>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(), RatingBar.OnRatingBarChangeListener {

    private var action : ((HotelData, ImageView) -> Unit) ?= null
    private var previousPosition = 0

    override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_view_home_activity_row,
                container,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listHotel.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val hotelData: HotelData = listHotel[position]
        Glide.with(context).load(Uri.parse(hotelData.imageUrl)).into(viewHolder.imgHotelImage)
        viewHolder.tvHotelName.text = hotelData.hotelName
        viewHolder.tvHotelAddress.text = hotelData.hotelAddress
        viewHolder.imgHotelImage.setOnClickListener {
            action?.invoke(hotelData, viewHolder.imgHotelImage)
        }
        viewHolder.ratingBar.onRatingBarChangeListener = this

        //Adding animation
        if (position > previousPosition){

        } else{

        }
    }

    fun setOnItemClickListener(action : (HotelData, ImageView) -> Unit){
        this.action = action
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgHotelImage: ImageView = view.findViewById(R.id.imgHotelPicture)
        val tvHotelName: TextView = view.findViewById(R.id.tvHotelNameBookingList)
        val tvHotelAddress: TextView = view.findViewById(R.id.tvHotelAddress)
        val ratingBar: RatingBar = view.findViewById(com.example.homestay.R.id.ratingBar)
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        Toast.makeText(context, "Rating changed, current rating " + ratingBar?.rating, Toast.LENGTH_SHORT).show()
    }

    fun getFilter(listHotelFiltered: MutableList<HotelData>) {
        listHotel = listHotelFiltered
        notifyDataSetChanged()
    }

}