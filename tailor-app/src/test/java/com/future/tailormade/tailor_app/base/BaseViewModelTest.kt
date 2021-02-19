package com.future.tailormade.tailor_app.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.test.BaseTest
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Rule

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseViewModelTest : BaseTest() {

  private val dispatcher = TestCoroutineDispatcher()

  @get:Rule
  val rule = CoroutineTestRule(dispatcher)

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  protected val savedStateHandle = mock<SavedStateHandle>()

  protected val authSharedPrefRepository = mock<AuthSharedPrefRepository>()

  private fun getError() = Exception()

  fun <T> getErrorFlow() = flow<T> {
    delay(1000)
    throw getError()
  }

  fun <T> getFlow(data: T) = flow {
    delay(1000)
    emit(data)
  }

  fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> {}
    try {
      observeForever(observer)
      block()
    } finally {
      removeObserver(observer)
    }
  }
}