package com.future.tailormade.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.view.ViewState
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.orFalse
import com.future.tailormade.util.logger.AppLogger
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

  protected var appLogger = AppLogger.create(this.getLogName())

  protected abstract fun getLogName(): String

  val viewState = MutableLiveData<ViewState>()

  private val _errorMessage = MutableLiveData<String?>()
  val errorMessage: LiveData<String?>
    get() = _errorMessage

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean>
    get() = _isLoading

  protected var page = Constants.INITIAL_PAGING_PAGE
  protected var itemPerPage = Constants.INITIAL_PAGING_ITEM_PER_PAGE

  fun isStillLoading() = _isLoading.value.orFalse()

  fun isFirstPage() = page == Constants.INITIAL_PAGING_PAGE

  protected fun setStartLoading() {
    _isLoading.value = true
  }

  protected fun setFinishLoading() {
    _isLoading.value = false
  }

  @ExperimentalCoroutinesApi
	open fun fetchMore() {
    page.inc()
  }

  @ExperimentalCoroutinesApi
	open fun refreshFetch() {
    page = Constants.INITIAL_PAGING_PAGE
  }

  fun setErrorMessage(message: String) {
    _errorMessage.value = message
  }

  fun <T> launchOnMainViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
    return launchOnViewModelScope(block, Dispatchers.Main)
  }

  fun <T> launchOnIOViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
    return launchOnViewModelScope(block, Dispatchers.IO)
  }

  fun <T> launchOnDefaultViewModelScope(block: suspend () -> LiveData<T>): LiveData<T> {
    return launchOnViewModelScope(block, Dispatchers.Default)
  }

  private fun <T> launchOnViewModelScope(block: suspend () -> LiveData<T>,
			coroutineContext: CoroutineContext): LiveData<T> {
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