package com.future.tailormade_profile.feature.profile

import com.future.tailormade_profile.base.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ProfileViewModelTest : BaseViewModelTest() {

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Test aja`() {

  }
}