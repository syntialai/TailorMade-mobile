package com.future.tailormade.feature.checkout

import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ThanksForOrderViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: ThanksForOrderViewModel

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)

//    whenever(savedStateHandle.getLiveData<MutableLiveData<CartUiModel>>(
//        ThanksForOrderViewModel.CART_UI_MODEL)) doReturn mock()
//    whenever(savedStateHandle.getLiveData<MutableLiveData<String>>(
//        ThanksForOrderViewModel.HISTORY_ID)) doReturn mock()
    viewModel = ThanksForOrderViewModel(savedStateHandle)

//    verify(savedStateHandle).getLiveData<MutableLiveData<CartUiModel>>(
//        ThanksForOrderViewModel.CART_UI_MODEL)
//    verify(savedStateHandle).getLiveData<MutableLiveData<String>>(
//        ThanksForOrderViewModel.HISTORY_ID)
  }

  @After
  override fun tearDown() {
//    verifyNoMoreInteractions(savedStateHandle)
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when set cart ui model then success update live data`() {
    val expectedResponse = PayloadMapper.getCartUiModel()
    viewModel.setCartUiModel(expectedResponse)
    viewModel.cartUiModel.observeForTesting {
      assertEquals(viewModel.cartUiModel.value, expectedResponse)
    }
//    assertTrue(savedStateHandle.contains(ThanksForOrderViewModel.CART_UI_MODEL))
  }

  @Test
  fun `Given when set history id then success update live data`() {
    viewModel.setHistoryId(ORDER_ID)
//    assertTrue(savedStateHandle.contains(ThanksForOrderViewModel.HISTORY_ID))
//    assertEquals(savedStateHandle.get(ThanksForOrderViewModel.HISTORY_ID), ORDER_ID)
  }
}