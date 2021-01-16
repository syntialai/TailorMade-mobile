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
import java.io.File

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

  fun convertFilePathToBitmap(filePath: String): Bitmap? = if (File(filePath).exists()) {
    BitmapFactory.decodeFile(filePath)
  } else {
    null
  }

  fun convertUrlToBitmap(context: Context, imageUrl: String): Bitmap = Glide.with(context).asBitmap().load(
      imageUrl).submit().get()

  fun getAdjustedBitmapSize(bitmapDestination: Bitmap, bitmapSource: Bitmap): Pair<Bitmap, Bitmap> {
    val maxWidth = maxOf(bitmapDestination.width, bitmapSource.width)
    val maxHeight = maxOf(bitmapDestination.height, bitmapSource.height)

    val overlayBitmapDestination = getOverlayImage(maxWidth, maxHeight, bitmapDestination)
    val overlayBitmapSource = getOverlayImage(maxWidth, maxHeight, bitmapSource)

    return Pair(overlayBitmapDestination, overlayBitmapSource)
  }

  private fun getOverlayImage(width: Int, height: Int, originalImage: Bitmap): Bitmap {
    val bitmapConfig = Bitmap.Config.ARGB_8888
    val newBitmap = Bitmap.createBitmap(width, height, bitmapConfig)
    return overlayBitmap(newBitmap, originalImage)
  }

  private fun overlayBitmap(bitmapDestination: Bitmap, bitmapSource: Bitmap): Bitmap {
    val bitmapOverlay = Bitmap.createBitmap(bitmapDestination.width, bitmapDestination.height,
        bitmapDestination.config)
    val canvas = Canvas(bitmapOverlay)
    canvas.drawBitmap(bitmapDestination, Matrix(), null)
    canvas.drawBitmap(bitmapSource, Matrix(), null)
    return bitmapOverlay
  }
}