package com.future.tailormade.feature.checkout

import com.future.tailormade.base.BaseViewModelTest
import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel
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
class CheckoutViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: CheckoutViewModel

  private val cartRepository = mock<CartRepository>()

  private val checkoutRepository = mock<CheckoutRepository>()

  private val idCaptor = argumentCaptor<String>()

  private val checkoutRequestCaptor = argumentCaptor<CheckoutRequest>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = CheckoutViewModel(cartRepository, checkoutRepository, authSharedPrefRepository,
        savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get cart item by id then success update live data`() {
    val expectedUiModel = PayloadMapper.getCartUiModel(true)
    val expectedMeasurements = PayloadMapper.getCartDesignSizeDetailUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.getCartById(USER_ID, CART_ID)) doReturn flow

      viewModel.setId(CART_ID)
      viewModel.id.observeForTesting {
        assertEquals(viewModel.id.value, CART_ID)
      }

      viewModel.getCartItemById(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).getCartById(USER_ID, CART_ID)
      assertLoading(true)
      delay(1000)

      viewModel.cartUiModel.observeForTesting {
        assertEquals(viewModel.cartUiModel.value, expectedUiModel)
      }
      assertMeasurements(expectedMeasurements)
      assertLoading(false)

      verifyNoMoreInteractions(authSharedPrefRepository, cartRepository)
    }
  }

  @Test
  fun `Given when get cart item by id and size detail response is null then success update live data`() {
    val expectedUiModel = PayloadMapper.getCartUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedUiModel)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.getCartById(USER_ID, CART_ID)) doReturn flow

      viewModel.setId(CART_ID)
      viewModel.id.observeForTesting {
        assertEquals(viewModel.id.value, CART_ID)
      }

      viewModel.getCartItemById(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).getCartById(USER_ID, CART_ID)
      assertLoading(true)
      delay(1000)

      viewModel.cartUiModel.observeForTesting {
        assertEquals(viewModel.cartUiModel.value, expectedUiModel)
      }
      assertMeasurements(null)
      assertLoading(false)

      verifyNoMoreInteractions(authSharedPrefRepository, cartRepository)
    }
  }

  @Test
  fun `Given when get cart item by id and error then update error message`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(cartRepository.getCartById(USER_ID, CART_ID)) doReturn getErrorFlow()

      viewModel.getCartItemById(CART_ID)

      verify(authSharedPrefRepository).userId
      verify(cartRepository).getCartById(USER_ID, CART_ID)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.FAILED_TO_GET_CHECKOUT_DATA)

      verifyNoMoreInteractions(authSharedPrefRepository, cartRepository)
    }
  }

  @Test
  fun `Given when get cart item by id and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.getCartItemById(CART_ID)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(cartRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when get checkout item by id then success update live data`() {
    val expectedResponse = PayloadMapper.getCheckoutResponse()
    val expectedRequest = PayloadMapper.getCheckoutRequest()
    val expectedMeasurements = PayloadMapper.getCartDesignSizeDetailUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(checkoutRepository.checkoutCartItem(any(), any(), any())) doReturn flow

      viewModel.setCheckoutMeasurementDetail(PayloadMapper.getCheckoutMeasurementRequestList())
      viewModel.measurementValues.observeForTesting {
        assertEquals(viewModel.measurementValues.value, expectedMeasurements)
      }

      viewModel.checkoutItem(CART_ID, expectedRequest.specialInstructions)

      verify(authSharedPrefRepository).userId
      verify(checkoutRepository).checkoutCartItem(idCaptor.capture(), idCaptor.capture(),
          checkoutRequestCaptor.capture())
      assertRequest(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      viewModel.historyId.observeForTesting {
        assertEquals(viewModel.historyId.value, ORDER_ID)
      }

      verifyNoMoreInteractions(authSharedPrefRepository, checkoutRepository)
    }
  }

  @Test
  fun `Given when get checkout item by id and error then update error message`() {
    val expectedRequest = PayloadMapper.getCheckoutRequest()
    val expectedMeasurements = PayloadMapper.getCartDesignSizeDetailUiModel()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(checkoutRepository.checkoutCartItem(any(), any(), any())) doReturn getErrorFlow()

      viewModel.setCheckoutMeasurementDetail(PayloadMapper.getCheckoutMeasurementRequestList())
      viewModel.measurementValues.observeForTesting {
        assertEquals(viewModel.measurementValues.value, expectedMeasurements)
      }

      viewModel.checkoutItem(CART_ID, expectedRequest.specialInstructions)

      verify(authSharedPrefRepository).userId
      verify(checkoutRepository).checkoutCartItem(idCaptor.capture(), idCaptor.capture(),
          checkoutRequestCaptor.capture())
      assertRequest(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.FAILED_TO_CHECKOUT_ITEM)

      verifyNoMoreInteractions(authSharedPrefRepository, checkoutRepository)
    }
  }

  @Test
  fun `Given when get checkout item by id and user id is null then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.checkoutItem(CART_ID, "")

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(checkoutRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertLoading(isLoading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, isLoading)
    }
  }

  private fun assertMeasurements(measurements: MutableList<String>?) {
    viewModel.measurementValues.observeForTesting {
      assertEquals(viewModel.measurementValues.value, measurements)
    }
  }

  private fun assertRequest(request: CheckoutRequest) {
    assertEquals(idCaptor.firstValue, USER_ID)
    assertEquals(idCaptor.secondValue, CART_ID)
    assertEquals(checkoutRequestCaptor.firstValue, request)
  }
}