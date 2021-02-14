package com.future.tailormade.feature.faceSwap.util

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object FileHelper {

  fun downloadImageFile(image: Bitmap): File {
    val fileName = generateFileName()
    val imageFile = File(getDirectory(), fileName)

    FileOutputStream(imageFile).apply {
      image.compress(Bitmap.CompressFormat.JPEG, 100, this)
      flush()
      close()
    }

    return imageFile
  }

  fun getImageUrlContentValues(contentResolver: ContentResolver): Uri? {
    val values = ContentValues().apply {
      put(MediaStore.Images.Media.TITLE, "title")
      put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
  }

  private fun getDirectory(): File {
    val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath.toString()
    val directory = File("$externalStoragePath/TailorMade")
    directory.mkdirs()
    return directory
  }

  private fun generateFileName() = String.format("%d.jpg", System.currentTimeMillis())
}