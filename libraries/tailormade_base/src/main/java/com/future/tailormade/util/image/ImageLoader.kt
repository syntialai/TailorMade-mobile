package com.future.tailormade.util.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

object ImageLoader {

  fun loadImageUrl(context: Context, imageUrl: String, imageView: ImageView) {
    val options = RequestOptions().fitCenter().centerCrop()
    Glide.with(context).load(imageUrl).apply(options).into(imageView)
  }

  fun loadImageResource(context: Context, imageResource: Int, imageView: ImageView) {
    Glide.with(context).load(imageResource).into(imageView)
  }

  fun loadImageFile(context: Context, imageFile: File, imageView: ImageView) {
    Glide.with(context).load(imageFile).into(imageView)
  }

  fun loadImageUrlWithFitCenterAndPlaceholder(context: Context, imageUrl: String, drawable: Int,
      imageView: ImageView) {
    val options = RequestOptions().fitCenter().centerCrop().placeholder(drawable)
    Glide.with(context).load(imageUrl).apply(options).into(imageView)
  }
}