package com.future.tailormade.util.view

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import com.future.tailormade.R

object DialogHelper {

  fun createLoadingDialog(context: Context): Dialog {
    return Dialog(context).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      setContentView(R.layout.layout_loading_dialog)
      setCancelable(false)
      setCanceledOnTouchOutside(false)
    }
  }

  fun showDialog(dialog: Dialog?) {
    if (dialog?.isShowing?.not() == true) {
      dialog.show()
    }
  }

  fun dismissDialog(dialog: Dialog?) {
    if (dialog != null && dialog.isShowing) {
      dialog.dismiss()
    }
  }
}