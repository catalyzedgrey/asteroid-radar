package com.example.asteroidradar.ui

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradar.R
import com.example.asteroidradar.domain.models.Asteroid
import com.example.asteroidradar.domain.models.PictureOfDay
import com.example.asteroidradar.ui.feed.FeedAdapter
import com.squareup.picasso.Picasso


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as FeedAdapter
    adapter.addHeaderAndSubmitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, img: PictureOfDay?) {
    img?.url?.let {
//        if (img.mediaType != "image")
//            return
        val imgUri = img.url.toUri()
        Picasso.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("setStatus")
fun bindStatus(imgView: ImageView, isPotentiallyHazardous: Boolean) {
    imgView.setImageResource(
        if (isPotentiallyHazardous)
            R.drawable.ic_status_potentially_hazardous
        else
            R.drawable.ic_status_normal
    )
}

