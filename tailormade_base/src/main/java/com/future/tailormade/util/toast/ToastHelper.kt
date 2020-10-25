package com.future.tailormade.util.toast

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.future.tailormade.R
import com.google.android.material.snackbar.Snackbar

object ToastHelper {

    fun showErrorToast(context: Context, view: View, message: String) {
        shortToast(view, message)
            .setBackgroundTint(ContextCompat.getColor(context, R.color.color_red_600))
            .setTextColor(ContextCompat.getColor(context, R.color.color_white))
            .show()
    }

    private fun shortToast(view: View, message: String): Snackbar {
        return Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    }
}