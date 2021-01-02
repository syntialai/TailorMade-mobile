package com.future.tailormade_face_swap.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import com.bumptech.glide.Glide
import com.future.tailormade.config.Constants

object BitmapHelper {

  fun compressImage(contentResolver: ContentResolver, image: Bitmap, imageUri: Uri) {
    try {
      val outputStream = contentResolver.openOutputStream(imageUri)
      image.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESS_IMAGE_QUALITY, outputStream)
      outputStream?.close()
    } catch (e: Exception) {
      System.err.println(e.toString())
    }
  }

  fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap = BitmapFactory.decodeByteArray(
      byteArray, 0, byteArray.size)

  fun convertUrlToBitmap(context: Context, url: String): Bitmap = Glide.with(context).asBitmap().load(
      url).submit().get()

  fun getAdjustedBitmapSize(bitmap1: Bitmap, bitmap2: Bitmap): Pair<Bitmap, Bitmap> {
    val maxWidth = maxOf(bitmap1.width, bitmap2.width)
    val maxHeight = maxOf(bitmap1.height, bitmap2.height)

    val overlayBitmap1 = getOverlayImage(maxWidth, maxHeight, bitmap1)
    val overlayBitmap2 = getOverlayImage(maxWidth, maxHeight, bitmap2)

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