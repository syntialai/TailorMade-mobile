package com.future.tailormade.util.view

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.future.tailormade.R
import com.google.android.material.snackbar.Snackbar

object ToastHelper {

    fun showToast(view: View, message: String, isLong: Boolean = false) = getToast(view, message,
        isLong).show()

    fun showErrorToast(context: Context, view: View, message: String) {
        getToast(view, message, false)
            .setBackgroundTint(ContextCompat.getColor(context, R.color.color_red_600))
            .setTextColor(ContextCompat.getColor(context, R.color.color_white))
            .show()
    }

    private fun getToast(view: View, message: String, isLong: Boolean): Snackbar = Snackbar.make(
        view, message, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
}