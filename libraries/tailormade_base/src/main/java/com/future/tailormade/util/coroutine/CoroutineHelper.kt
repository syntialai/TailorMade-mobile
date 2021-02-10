package com.future.tailormade.util.coroutine

import com.future.tailormade.config.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CoroutineHelper {

  fun <T> debounce(delayMs: Long = Constants.DEBOUNCE_TIME_300, scope: CoroutineScope,
      destinationFunction: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
      debounceJob?.cancel()
      debounceJob = scope.launch {
        delay(delayMs)
        destinationFunction(param)
      }
    }
  }
}