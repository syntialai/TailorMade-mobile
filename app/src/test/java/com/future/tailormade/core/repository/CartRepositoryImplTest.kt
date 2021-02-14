package com.future.tailormade.core.repository

import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.repository.impl.CartRepositoryImpl
import com.future.tailormade.core.service.CartService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CartRepositoryImplTest : BaseTest() {

  private lateinit var cartRepository: CartRepository

  @Mock
  private lateinit var cartService: CartService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    cartRepository = CartRepositoryImpl(cartService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get carts then success return data`() {
    val expectedResponse = generateListBaseResponse(data = PayloadMapper.getCartResponses())
    val expectedUiModel = PayloadMapper.getCartUiModels()

    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { getCarts(USER_ID, PAGE, ITEM_PER_PAGE) } doReturn expectedResponse
      }

      val flow = cartRepository.getCarts(USER_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        verify(cartService).getCarts(USER_ID, PAGE, ITEM_PER_PAGE)
        assertEquals(it, expectedUiModel)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when get carts and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { getCarts(USER_ID, PAGE, ITEM_PER_PAGE) } doReturn generateListBaseResponse()
      }

      val flow = cartRepository.getCarts(USER_ID, PAGE, ITEM_PER_PAGE)

      flow.collect {
        verify(cartService).getCarts(USER_ID, PAGE, ITEM_PER_PAGE)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when get cart by id then success return mapped data`() {
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getCartResponse())
    val expectedUiModel = PayloadMapper.getCartUiModel()

    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { getCartById(USER_ID, CART_ID) } doReturn expectedResponse
      }

      val flow = cartRepository.getCartById(USER_ID, CART_ID)

      flow.collect {
        verify(cartService).getCartById(USER_ID, CART_ID)
        assertEquals(it, expectedUiModel)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when get cart by id and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { getCartById(USER_ID, CART_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = cartRepository.getCartById(USER_ID, CART_ID)

      flow.collect {
        verify(cartService).getCartById(USER_ID, CART_ID)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when edit cart quantity by id then success return data`() {
    val request = PayloadMapper.getEditCartQuantityRequest()
    val expectedResponse = generateSingleBaseResponse(
        data = PayloadMapper.getEditCartQuantityResponse())

    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { putEditCartItemQuantity(USER_ID, CART_ID, request) } doReturn expectedResponse
      }

      val flow = cartRepository.editCartItemQuantity(USER_ID, CART_ID, request)

      flow.collect {
        verify(cartService).putEditCartItemQuantity(USER_ID, CART_ID, request)
        assertEquals(it, expectedResponse.data)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when edit cart quantity by id and data returned is null then do nothing`() {
    val request = PayloadMapper.getEditCartQuantityRequest()

    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking {
          putEditCartItemQuantity(USER_ID, CART_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = cartRepository.editCartItemQuantity(USER_ID, CART_ID, request)

      flow.collect {
        verify(cartService).putEditCartItemQuantity(USER_ID, CART_ID, request)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when delete cart by id then success return data`() {
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getDeleteCartResponse())

    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { deleteCartItemById(USER_ID, CART_ID) } doReturn expectedResponse
      }

      val flow = cartRepository.deleteCartItemById(USER_ID, CART_ID)

      flow.collect {
        verify(cartService).deleteCartItemById(USER_ID, CART_ID)
        assertEquals(it, expectedResponse.data)
        verifyNoMoreInteractions(cartService)
      }
    }
  }

  @Test
  fun `Given when delete cart by id and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      cartService.stub {
        onBlocking { deleteCartItemById(USER_ID, CART_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = cartRepository.deleteCartItemById(USER_ID, CART_ID)

      flow.collect {
        verify(cartService).deleteCartItemById(USER_ID, CART_ID)
        verifyNoMoreInteractions(cartService)
      }
    }
  }
}