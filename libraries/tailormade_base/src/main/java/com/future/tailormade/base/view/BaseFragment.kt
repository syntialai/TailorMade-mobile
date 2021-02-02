package com.future.tailormade.base.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.logger.AppLogger
import com.future.tailormade.util.view.DialogHelper
import com.future.tailormade.util.view.ToastHelper
import com.future.tailormade_router.actions.Action

abstract class BaseFragment : Fragment() {

  abstract fun getLogName(): String

  open fun getScreenName(): String = ""

  open fun onNavigationIconClicked(): Unit? = null

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
      (activity as BaseActivity).setupOnNavigationIconClicked(::onNavigationIconClicked)
      activity.setupToolbar(getScreenName())
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
    getViewModel()?.isLoading?.observe(viewLifecycleOwner, { isLoading ->
//      when (state) {
//        is ViewState.Loading -> onLoading(state.isLoading)
//        is ViewState.Unauthorized -> onUnauthorized()
//        else -> showNoInternetConnection()
//      }
      isLoading?.let {
        onLoading(it)
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
    context?.let {
      Action.goToSignIn(it)
      activity?.finish()
    }
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

  fun isLastItemViewed(recyclerView: RecyclerView, lastItemPosition: Int): Boolean {
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    return layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition
  }

  fun hideToolbar() {
    activity?.let { activity ->
      (activity as BaseActivity).hideToolbar()
    }
  }

  fun showSuccessToast(messageId: Int) {
    activity?.findViewById<View>(android.R.id.content)?.let {
      ToastHelper.showToast(it, getString(messageId))
    }
  }

  fun showToolbar() {
    activity?.let { activity ->
      (activity as BaseActivity).showToolbar()
    }
  }

  private fun showLoadingView() {
    if (loadingDialog == null) {
      loadingDialog = DialogHelper.createLoadingDialog(requireContext())
    }
    DialogHelper.showDialog(loadingDialog)
  }

  private fun hideLoadingView() {
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