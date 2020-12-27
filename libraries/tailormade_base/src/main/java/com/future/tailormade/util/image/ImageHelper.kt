package com.future.tailormade.util.image

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns




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
}