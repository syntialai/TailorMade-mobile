package com.future.tailormade_face_swap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import com.bumptech.glide.Glide

object BitmapHelper {

  fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap = BitmapFactory.decodeByteArray(
      byteArray, 0, byteArray.size)

  fun convertUrlToBitmap(context: Context, url: String): Bitmap = Glide.with(context).asBitmap().load(
      url).submit().get()

  fun getOverlayedImage(width: Int, height: Int, originalImage: Bitmap): Bitmap {
    val bitmapConfig = Bitmap.Config.ARGB_8888
    val newBitmap = Bitmap.createBitmap(width, height, bitmapConfig)
    return overlayBitmap(newBitmap, originalImage)
  }

  private fun overlayBitmap(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap {
    val bmOverlay = Bitmap.createBitmap(bitmap1.width, bitmap1.height, bitmap1.config)
    val canvas = Canvas(bmOverlay)
    canvas.drawBitmap(bitmap1, Matrix(), null)
    canvas.drawBitmap(bitmap2, Matrix(), null)
    return bmOverlay
  }
}