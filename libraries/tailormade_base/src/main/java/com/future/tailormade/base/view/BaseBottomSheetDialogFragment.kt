package com.future.tailormade.base.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.future.tailormade.util.logger.AppLogger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

  protected var appLogger = AppLogger.create(this.getScreenName())

  abstract fun getScreenName(): String

  override fun onAttach(context: Context) {
    appLogger.logLifecycleOnAttach()
    super.onAttach(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    appLogger.logLifecycleOnCreate()
    super.onCreate(savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    appLogger.logLifecycleOnActivityCreated()
    super.onActivityCreated(savedInstanceState)
  }

  override fun onStart() {
    appLogger.logLifecycleOnStart()
    super.onStart()
  }

  override fun onResume() {
    appLogger.logLifecycleOnResume()
    super.onResume()
  }

  override fun onPause() {
    appLogger.logLifecycleOnPause()
    super.onPause()
  }

  override fun onStop() {
    appLogger.logLifecycleOnStop()
    super.onStop()
  }

  override fun onDestroy() {
    appLogger.logLifecycleOnDestroy()
    super.onDestroy()
  }

  override fun onDestroyView() {
    appLogger.logLifecycleOnDestroyView()
    super.onDestroyView()
  }

  override fun onDetach() {
    appLogger.logLifecycleOnDetach()
    super.onDetach()
  }

  fun hideKeyboard() {
    view?.let { view ->
      val inputManager = activity?.getSystemService(
          Context.INPUT_METHOD_SERVICE) as InputMethodManager
      inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }
}