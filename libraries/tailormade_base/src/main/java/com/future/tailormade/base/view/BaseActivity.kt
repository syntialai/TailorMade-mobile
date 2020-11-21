package com.future.tailormade.base.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.future.tailormade.util.logger.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity() {

  open fun getScreenName(): String = "BaseActivity"

  protected var appLogger = AppLogger.create(this.getScreenName())

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

  fun launchCoroutineOnMain(block: () -> Unit) {
    launchCoroutine(Dispatchers.Main, block)
  }

  fun launchCoroutineOnIO(block: () -> Unit) {
    launchCoroutine(Dispatchers.IO, block)
  }

  private fun launchCoroutine(coroutineContext: CoroutineContext, block: () -> Unit) {
    CoroutineScope(coroutineContext).launch {
      block()
    }
  }
}