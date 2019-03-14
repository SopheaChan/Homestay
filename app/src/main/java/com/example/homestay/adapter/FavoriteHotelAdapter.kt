package com.example.homestay.adapter

import android.app.Activity
import android.net.Uri
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.homestay.R
import com.example.homestay.model.FavoriteList

class FavoriteHotelAdapter(private val listFavoriteHotel: ArrayList<FavoriteList?>, private val context: Activity) :
    RecyclerView.Adapter<FavoriteHotelAdapter.ViewHolder>() {

    private var action: ((FavoriteList?, ArrayList<FavoriteList?>)-> Unit) ?= null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context.baseContext)
        val view = inflater.inflate(R.layout.recycler_favorite_hotel_row, container, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFavoriteHotel.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val myFavoriteHotel: FavoriteList ?= listFavoriteHotel[position]

        viewHolder.tvName.text = myFavoriteHotel?.hotelName ?: "N/A"
        viewHolder.tvAddress.text = myFavoriteHotel?.address ?: "N/A"
        Glide.with(context).load(Uri.parse(myFavoriteHotel?.imageUrl)).into(viewHolder.imgHotelImage)
//        viewHolder.tvIssuedDate.text = myFavoriteHotel?.issueDate ?: "N/A"
        viewHolder.btnRemoveFromFavorite.setOnClickListener{
            action?.invoke(myFavoriteHotel, listFavoriteHotel)
            notifyDataSetChanged()
        }

    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvFavoriteHotelName)
        val tvAddress: TextView = view.findViewById(R.id.tvFavoriteHotelAddress)
        val imgHotelImage: ImageView = view.findViewById(R.id.imgFavoriteHotelPicture)
        val btnRemoveFromFavorite: TextView = view.findViewById(R.id.btnRemoveFromFavorite)
//        val tvIssuedDate: TextView = view.findViewById(R.id.tvDate)
//        val btnRemoveFromFavoriteList: AppCompatButton = view.findViewById(R.id.btnRemoveFromFavorite)
    }

    //Closure callback
    fun onRemoveFromListClicklistener(action: (FavoriteList?, ArrayList<FavoriteList?>) -> Unit){
        this.action = action
    }
}