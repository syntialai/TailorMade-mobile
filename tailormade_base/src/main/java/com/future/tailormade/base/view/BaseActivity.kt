package com.future.tailormade.base.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.future.tailormade.util.logger.AppLogger

abstract class BaseActivity : AppCompatActivity() {

    protected var appLogger = AppLogger.create(this.getScreenName())

    open fun getScreenName(): String = "com.future.tailormade.base.view.BaseActivity"

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
}