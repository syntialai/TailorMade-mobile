package com.future.tailormade_chat.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.test.BaseTest
import com.google.android.gms.tasks.Task
import com.google.firebase.database.Query
import com.google.firebase.database.core.Repo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import java.lang.Exception
import java.util.Objects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Rule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseViewModelTest : BaseTest() {

  companion object {
    const val ANOTHER_USER_ID = "ANOTHER USER ID"
    const val ANOTHER_USER_NAME = "ANOTHER USER NAME"
  }

  val dispatcher = TestCoroutineDispatcher()

  @get:Rule
  val rule = CoroutineTestRule(dispatcher)

  @get:Rule
  val taskExecutorRule = InstantTaskExecutorRule()

  protected val savedStateHandle: SavedStateHandle = mock()

  protected val authSharedPrefRepository: AuthSharedPrefRepository = mock()

  fun getError() = Exception()

  fun anyQuery() = mock<Query>()

  fun anyTask() = mock<Task<Void>>()

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