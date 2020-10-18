package com.future.tailormade.base.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.future.tailormade.util.logger.AppLogger

abstract class BaseFragment : Fragment() {

    protected var appLogger = AppLogger.create(this.getScreenName())

    open fun getScreenName(): String = "com.future.tailormade.base.view.BaseFragment"

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
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}