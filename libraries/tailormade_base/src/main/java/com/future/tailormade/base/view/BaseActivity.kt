package com.future.tailormade.base.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.future.tailormade.util.logger.AppLogger

abstract class BaseActivity : AppCompatActivity() {

    protected var appLogger = AppLogger.create(this.getScreenName())

    open fun getScreenName(): String = "BaseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        appLogger.logLifecycleOnCreate()
        super.onCreate(savedInstanceState)
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

    fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val inputManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}