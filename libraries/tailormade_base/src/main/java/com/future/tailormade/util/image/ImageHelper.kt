package com.future.tailormade.util.image

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.future.tailormade.R
import com.future.tailormade.base.model.enums.GenderEnum

object ImageHelper {

  fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme.equals("content")) {
      val cursor = contentResolver.query(uri, null, null, null, null)
      cursor?.use { cursor ->
        if (cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
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

  fun getUserProfilePlaceholder(genderEnumValue: Int) = when (GenderEnum.values()[genderEnumValue]) {
    GenderEnum.Female -> R.drawable.illustration_female
    GenderEnum.Male -> R.drawable.illustration_male
    else -> R.drawable.ic_profile
  }
}