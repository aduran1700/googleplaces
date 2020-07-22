package com.googleplaces.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null && url != "") {
        Glide.with(imageView.context).load(url).into(imageView)
    }
}
