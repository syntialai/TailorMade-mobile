package com.future.tailormade.base.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.logger.AppLogger
import com.future.tailormade.util.view.DialogHelper
import com.future.tailormade.util.view.ToastHelper

abstract class BaseFragment : Fragment() {

  abstract fun getLogName(): String

  open fun getScreenName(): String = ""

  protected var appLogger = AppLogger.create(this.getLogName())

  protected abstract fun getViewModel(): BaseViewModel?

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

    activity?.let { activity ->
      (activity as BaseActivity).setupToolbar(getScreenName())
    }

    setupFragmentObserver()
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

  open fun setupFragmentObserver() {
    getViewModel()?.viewState?.observe(viewLifecycleOwner, { state ->
      when (state) {
        is ViewState.Loading -> onLoading(state.isLoading)
        is ViewState.Unauthorized -> onUnauthorized()
        else -> showNoInternetConnection()
      }
    })

    getViewModel()?.errorMessage?.observe(viewLifecycleOwner, { error ->
      hideKeyboard()
      if (error != null && context != null && view != null) {
        ToastHelper.showErrorToast(requireContext(), requireView(), error)
      }
    })
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

  open fun isLastItemViewed(recyclerView: RecyclerView, lastItemPosition: Int): Boolean {
    val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
    return getViewModel()?.isStillLoading()?.not().orFalse() &&
           layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition
  }

  fun hideToolbar() {
    activity?.let { activity ->
      (activity as BaseActivity).hideToolbar()
    }
  }

  fun showToolbar() {
    activity?.let { activity ->
      (activity as BaseActivity).showToolbar()
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