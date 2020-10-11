package com.future.tailormade.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.future.tailormade.util.AppLogger

abstract class BaseFragment: Fragment() {

    protected var appLogger = AppLogger.create(this.getScreenName())

    open fun getScreenName(): String = "com.future.tailormade.base.BaseFragment"

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
}