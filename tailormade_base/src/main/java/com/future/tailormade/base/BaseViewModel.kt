package com.future.tailormade.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.future.tailormade.util.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected var appLogger = AppLogger.create(this.getLogName())

    protected abstract fun getLogName(): String

    fun <T> launchOnMainViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
        return launchOnViewModelScope(block, Dispatchers.Main)
    }

    fun <T> launchOnIOViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
        return launchOnViewModelScope(block, Dispatchers.IO)
    }

    fun <T> launchOnDefaultViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
        return launchOnViewModelScope(block, Dispatchers.Default)
    }

    private fun <T> launchOnViewModelScope(
        block: suspend () -> LiveData<T>,
        coroutineContext: CoroutineContext
    ): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + coroutineContext) {
            emitSource(block())
        }
    }

    fun launchViewModelScope(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}