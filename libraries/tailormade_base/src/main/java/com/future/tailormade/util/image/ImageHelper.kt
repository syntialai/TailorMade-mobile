package com.future.tailormade.util.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.annotation.RequiresApi
import com.future.tailormade.R
import com.future.tailormade.base.model.enums.GenderEnum
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64.getDecoder

object ImageHelper {

  fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme.equals("content")) {
      val cursor = contentResolver.query(uri, null, null, null, null)
      cursor.use { cursor ->
        cursor?.let {
          if (it.moveToFirst()) {
            result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
          }
        }
      }
    }
    if (result == null) {
      val cut = uri.path?.lastIndexOf('/')
      if (cut != null && cut != -1) {
        result = result?.substring(cut + 1)
      }
    }
    return result
  }

  fun encodeAndCompressFile(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray: ByteArray = outputStream.toByteArray()
    return "data:image/jpg;base64," + encodeToString(byteArray, Base64.DEFAULT)
  }

  fun getUserProfilePlaceholder(genderEnumValue: Int) = when (GenderEnum.values()[genderEnumValue]) {
    GenderEnum.Female -> R.drawable.illustration_female
    GenderEnum.Male -> R.drawable.illustration_male
    else -> R.drawable.ic_profile
  }
}