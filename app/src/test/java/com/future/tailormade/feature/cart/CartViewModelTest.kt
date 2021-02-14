package com.future.tailormade.feature.cart

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.feature.cart.viewModel.CartViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CartViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: CartViewModel
  
  private val cartRepository = mock<CartRepository>()

  private val editQuantityCaptor = argumentCaptor<String>()

  private val editQuantityRequestCaptor = argumentCaptor<CartEditQuantityRequest>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = CartViewModel(cartRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }
  
  @Test
  fun `Given when fetch cart data then success update live data`() {
    val expectedUiModel = PayloadMapper.getCartUiModels()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.getCarts(USER_ID, PAGE, ITEM_PER_PAGE)) doReturn flow

      viewModel.fetchCartData()

      verify(authSharedPrefRepository).userId
      verify(cartRepository).getCarts(USER_ID, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.cartUiModel.observeForTesting {
        assertEquals(viewModel.cartUiModel.value, expectedUiModel)
      }
      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch cart data and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.getCarts(USER_ID, PAGE + 1, ITEM_PER_PAGE)) doReturn getErrorFlow()

      viewModel.fetchMore()

      verify(authSharedPrefRepository).userId
      verify(cartRepository).getCarts(USER_ID, PAGE + 1, ITEM_PER_PAGE)
      delay(1000)

      assertError(Constants.FAILED_TO_GET_YOUR_CART_ITEM)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch cart data and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.fetchCartData()

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(cartRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when delete cart item and data is exist then success update live data`() {
    val carts = PayloadMapper.getCartUiModels()
    val expectedUiModel = PayloadMapper.getDeleteCartResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.deleteCartItemById(USER_ID, CART_ID)) doReturn flow

      viewModel.setCartUiModel(carts)
      assertCarts(carts)

      viewModel.deleteCartItem(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).deleteCartItemById(USER_ID, CART_ID)
      delay(1000)

      assertCarts(arrayListOf())
      assertHasBeenDeleted(true)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when delete cart item and data is not exist then success update live data`() {
    val expectedUiModel = PayloadMapper.getDeleteCartResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.deleteCartItemById(USER_ID, CART_ID)) doReturn flow

      viewModel.deleteCartItem(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).deleteCartItemById(USER_ID, CART_ID)
      delay(1000)

      assertCarts(null)
      assertHasBeenDeleted(true)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when delete cart item and error then update error message`() {
    val carts = PayloadMapper.getCartUiModels()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.deleteCartItemById(USER_ID, CART_ID)) doReturn getErrorFlow()

      viewModel.setCartUiModel(carts)
      assertCarts(carts)

      viewModel.deleteCartItem(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).deleteCartItemById(USER_ID, CART_ID)
      delay(1000)

      assertError(Constants.FAILED_TO_DELETE_CART_ITEM)
      assertCarts(carts)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when delete cart item and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.deleteCartItem(CART_ID)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(cartRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when edit cart item quantity and data is exist then success update live data`() {
    val carts = PayloadMapper.getCartUiModels()
    val expectedRequest = PayloadMapper.getEditCartQuantityRequest(2)
    val expectedResponse = PayloadMapper.getEditCartQuantityResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.editCartItemQuantity(any(), any(), any())) doReturn flow

      viewModel.setCartUiModel(carts)
      assertCarts(carts)

      viewModel.editCartItemQuantity(CART_ID, DESIGN_QUANTITY + 1)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).editCartItemQuantity(editQuantityCaptor.capture(),
          editQuantityCaptor.capture(), editQuantityRequestCaptor.capture())
      assertCaptors(expectedRequest)
      delay(1000)

      assertCarts(carts.apply {
        this[0].quantity = 2
      })

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when edit cart item quantity and data is not exist then success update live data`() {
    val expectedRequest = PayloadMapper.getEditCartQuantityRequest(2)
    val expectedResponse = PayloadMapper.getEditCartQuantityResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.editCartItemQuantity(any(), any(), any())) doReturn flow

      viewModel.editCartItemQuantity(CART_ID, DESIGN_QUANTITY + 1)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).editCartItemQuantity(editQuantityCaptor.capture(),
          editQuantityCaptor.capture(), editQuantityRequestCaptor.capture())
      assertCaptors(expectedRequest)
      delay(1000)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when edit cart item quantity and error then update error message`() {
    val expectedRequest = PayloadMapper.getEditCartQuantityRequest(2)

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.editCartItemQuantity(any(), any(), any())) doReturn getErrorFlow()

      viewModel.editCartItemQuantity(CART_ID, DESIGN_QUANTITY + 1)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).editCartItemQuantity(editQuantityCaptor.capture(),
          editQuantityCaptor.capture(), editQuantityRequestCaptor.capture())
      assertCaptors(expectedRequest)
      delay(1000)

      assertError(Constants.FAILED_TO_UPDATE_YOUR_CART_ITEM)

      verifyNoMoreInteractions(cartRepository, authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when edit cart item quantity and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.editCartItemQuantity(CART_ID, DESIGN_QUANTITY + 1)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(cartRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertCarts(carts: ArrayList<CartUiModel>?) {
    viewModel.cartUiModel.observeForTesting {
      assertEquals(viewModel.cartUiModel.value, carts)
    }
  }

  private fun assertCaptors(request: CartEditQuantityRequest) {
    assertEquals(editQuantityCaptor.firstValue, USER_ID)
    assertEquals(editQuantityCaptor.secondValue, CART_ID)
    assertEquals(editQuantityRequestCaptor.firstValue, request)
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertHasBeenDeleted(value: Boolean) {
    viewModel.hasBeenDeleted.observeForTesting {
      assertEquals(viewModel.hasBeenDeleted.value, value)
    }
  }
}