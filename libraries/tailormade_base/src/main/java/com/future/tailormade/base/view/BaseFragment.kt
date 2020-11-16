package com.future.tailormade.base.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.logger.AppLogger
import com.future.tailormade.util.view.DialogHelper
import com.future.tailormade.util.view.ToastHelper

abstract class BaseFragment : Fragment() {

  protected var appLogger = AppLogger.create(this.getScreenName())

  abstract fun getScreenName(): String

  protected open fun getViewModel(): BaseViewModel? = null

  protected open var loadingDialog: Dialog? = null

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

    getViewModel()?.viewState?.observe(viewLifecycleOwner, { state ->
      when (state) {
        is ViewState.Loading -> onLoading(state.isLoading)
        is ViewState.Unauthorized -> onUnauthorized()
        else -> showNoInternetConnection()
      }
    })

    getViewModel()?.errorMessage?.observe(viewLifecycleOwner, { error ->
      if (error != null && context != null && view != null) {
        ToastHelper.showErrorToast(requireContext(), requireView(), error)
      }
    })
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

  private fun onUnauthorized() {
    // TODO: Implement this
  }

  private fun showNoInternetConnection() {
    // TODO: Implement this
  }

  private fun onLoading(isLoading: Boolean) {
    if (isLoading) {
      showLoadingView()
    } else {
      hideLoadingView()
    }
  }

  fun showLoadingView() {
    if (loadingDialog == null && context == null) {
      loadingDialog = DialogHelper.createLoadingDialog(requireContext())
    }
    DialogHelper.showDialog(loadingDialog)
  }

  fun hideLoadingView() {
    DialogHelper.dismissDialog(loadingDialog)
    loadingDialog = null
  }

  fun hideKeyboard() {
    view?.let { view ->
      val inputManager =
        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }
}