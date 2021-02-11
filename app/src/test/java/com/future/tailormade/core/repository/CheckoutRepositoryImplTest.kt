package com.future.tailormade.core.repository

import com.future.tailormade.base.PayloadMapper
import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.repository.impl.CheckoutRepositoryImpl
import com.future.tailormade.core.service.CheckoutService
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
class CheckoutRepositoryImplTest : BaseTest() {

  private lateinit var checkoutRepository: CheckoutRepository

  @Mock
  private lateinit var checkoutService: CheckoutService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    checkoutRepository = CheckoutRepositoryImpl(checkoutService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when checkout cart item then success return data`() {
    val request = PayloadMapper.getCheckoutRequest()
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getCheckoutResponse())

    dispatcher.runBlockingTest {
      checkoutService.stub {
        onBlocking { postCheckoutCartItem(USER_ID, CART_ID, request) } doReturn expectedResponse
      }

      val flow = checkoutRepository.checkoutCartItem(USER_ID, CART_ID, request)

      flow.collect {
        verify(checkoutService).postCheckoutCartItem(USER_ID, CART_ID, request)
        assertEquals(it, expectedResponse.data)
        verifyNoMoreInteractions(checkoutService)
      }
    }
  }

  @Test
  fun `Given when checkout cart item and data returned is null then do nothing`() {
    val request = PayloadMapper.getCheckoutRequest()

    dispatcher.runBlockingTest {
      checkoutService.stub {
        onBlocking {
          postCheckoutCartItem(USER_ID, CART_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = checkoutRepository.checkoutCartItem(USER_ID, CART_ID, request)

      flow.collect {
        verify(checkoutService).postCheckoutCartItem(USER_ID, CART_ID, request)
        verifyNoMoreInteractions(checkoutService)
      }
    }
  }
}