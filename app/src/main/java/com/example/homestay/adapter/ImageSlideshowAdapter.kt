package com.example.homestay.adapter

import android.content.Context
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.homestay.R

class ImageSlideshowAdapter constructor(private val context: Context, private val images: ArrayList<String>) : PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    //p0 -> view: View, p1-> object: Object
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return (p0 == p1)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.layout_image_for_view_detail, container, false)
        val img: ImageView = view.findViewById(R.id.imageView)
//        img.setImageResource(images[position])
        //Set image to imageView using Glide, since Glide is faster than Picasso
        Glide.with(context).load(Uri.parse(images[position])).into(img)
        //Add eventClickListener to each image
        img.setOnClickListener {
            Snackbar.make(img, "Images: " + (position + 1).toString(), 1000).show()
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}