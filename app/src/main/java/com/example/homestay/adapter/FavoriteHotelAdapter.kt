package com.example.homestay.adapter

import android.app.Activity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.homestay.R
import com.example.homestay.model.FavoriteList

class FavoriteHotelAdapter(private val listFavoriteHotel: ArrayList<FavoriteList?>, private val context: Activity) :
    RecyclerView.Adapter<FavoriteHotelAdapter.ViewHolder>() {

    private var action: ((FavoriteList?, ArrayList<FavoriteList?>)-> Unit) ?= null

    override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
        val inflater = LayoutInflater.from(context.baseContext)
        val view = inflater.inflate(R.layout.layout_favorite_list_row, container, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFavoriteHotel.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val myFavoriteHotel: FavoriteList ?= listFavoriteHotel[position]

        viewHolder.tvName.text = myFavoriteHotel?.hotelName ?: "N/A"
        viewHolder.tvAddress.text = myFavoriteHotel?.address ?: "N/A"
        viewHolder.tvIssuedDate.text = myFavoriteHotel?.issueDate ?: "N/A"
        viewHolder.btnRemoveFromFavoriteList.setOnClickListener{
            action?.invoke(myFavoriteHotel, listFavoriteHotel)
            notifyDataSetChanged()
        }

    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvIssuedDate: TextView = view.findViewById(R.id.tvDate)
        val btnRemoveFromFavoriteList: AppCompatButton = view.findViewById(R.id.btnRemoveFromFavorite)
    }

    //Closure callback
    fun onRemoveFromListClicklistener(action: (FavoriteList?, ArrayList<FavoriteList?>) -> Unit){
        this.action = action
    }
}