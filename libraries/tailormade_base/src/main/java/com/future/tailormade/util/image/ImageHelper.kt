package com.future.tailormade.util.image

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import java.io.File
import java.util.*

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

  @RequiresApi(Build.VERSION_CODES.O)
  fun encodeFile(filePath: String): String{
    val bytes = File(filePath).readBytes()
    return Base64.getEncoder().encodeToString(bytes)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun decoder(base64Str: String, pathFile: String) {
    val imageByteArray = Base64.getDecoder().decode(base64Str)
    File(pathFile).writeBytes(imageByteArray)
  }
}