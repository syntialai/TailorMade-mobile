package com.future.tailormade.util.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

object ImageLoader {

  fun loadImageUrl(context: Context, imageUrl: String, imageView: ImageView, isCircle: Boolean = false) {
    if (isCircle) {
      Glide.with(context).load(imageUrl).circleCrop().into(imageView)
    } else {
      Glide.with(context).load(imageUrl).centerInside().into(imageView)
    }
  }

  fun loadImageResource(context: Context, imageResource: Int, imageView: ImageView) {
    Glide.with(context).load(imageResource).into(imageView)
  }

  fun loadImageFile(context: Context, imageFile: File, imageView: ImageView) {
    Glide.with(context).load(imageFile).into(imageView)
  }

  fun loadImageUrlWithFitCenterAndPlaceholder(context: Context, imageUrl: String, drawable: Int,
      imageView: ImageView, isCircle: Boolean = false) {
    if (isCircle) {
      Glide.with(context).load(imageUrl).placeholder(drawable).circleCrop().centerInside().into(
          imageView)
    } else {
      Glide.with(context).load(imageUrl).centerInside().placeholder(drawable).into(imageView)
    }
  }
}