package com.future.tailormade_face_swap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import com.bumptech.glide.Glide
import java.io.File

object BitmapHelper {

  fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap = BitmapFactory.decodeByteArray(
      byteArray, 0, byteArray.size)

  fun convertFilePathToBitmap(filePath: String): Bitmap? = if (File(filePath).exists()) {
    BitmapFactory.decodeFile(filePath)
  } else {
    null
  }

  fun convertUrlToBitmap(context: Context, url: String): Bitmap = Glide.with(context).asBitmap().load(
      url).submit().get()

  fun getAdjustedBitmapSize(bitmapDestination: Bitmap, bitmapSource: Bitmap): Pair<Bitmap, Bitmap> {
    val maxWidth = maxOf(bitmapDestination.width, bitmapSource.width)
    val maxHeight = maxOf(bitmapDestination.height, bitmapSource.height)

    val overlayBitmap1 = getOverlayImage(maxWidth, maxHeight, bitmapDestination)
    val overlayBitmap2 = getOverlayImage(maxWidth, maxHeight, bitmapSource)

    return Pair(overlayBitmap1, overlayBitmap2)
  }

  private fun getOverlayImage(width: Int, height: Int, originalImage: Bitmap): Bitmap {
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