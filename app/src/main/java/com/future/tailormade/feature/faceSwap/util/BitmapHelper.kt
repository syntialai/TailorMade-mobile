package com.future.tailormade.feature.faceSwap.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
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

  fun convertFileToBitmap(contentResolver: ContentResolver, mediaUri: Uri): Bitmap? {
    var bitmapResult: Bitmap? = null

    try {
      bitmapResult = MediaStore.Images.Media.getBitmap(contentResolver, mediaUri)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return bitmapResult
  }

  fun getUrlToBitmapRequestBuilder(context: Context, imageUrl: String): RequestBuilder<Bitmap> = Glide.with(
      context).asBitmap().load(imageUrl)

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