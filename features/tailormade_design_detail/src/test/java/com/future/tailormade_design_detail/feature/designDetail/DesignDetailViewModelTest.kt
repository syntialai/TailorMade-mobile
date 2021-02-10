package com.future.tailormade_design_detail.feature.designDetail

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.config.Constants
import com.future.tailormade_design_detail.base.BaseViewModelTest
import com.future.tailormade_design_detail.base.PayloadMapper
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartRequest
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.feature.designDetail.viewModel.DesignDetailViewModel
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
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
class DesignDetailViewModelTest : BaseViewModelTest() {

  companion object {
    private const val TYPE = "TYPE"
  }

  private lateinit var viewModel: DesignDetailViewModel

  private val designDetailRepository = mock<DesignDetailRepository>()

  private val userIdCaptor = argumentCaptor<String>()

  private val addToCartRequestCaptor = argumentCaptor<AddToCartRequest>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = DesignDetailViewModel(designDetailRepository, authSharedPrefRepository,
        savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when add to cart then success update live data`() {
    val expectedRequest = PayloadMapper.getAddToCartRequest()
    val expectedResponse = PayloadMapper.getAddToCartResponse()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(expectedResponse)

      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(designDetailRepository.addToCart(USER_ID, expectedRequest)) doReturn flow

      addRequest()
      delay(1000)
      viewModel.addToCart(TYPE)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).addToCart(userIdCaptor.capture(),
          addToCartRequestCaptor.capture())
      assertAddToCartRequest(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertIsAddedToCart(expectedResponse.wishlistId)
      assertLoading(false)

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @Test
  fun `Given when add to cart and error when add to cart then update live data`() {
    val expectedRequest = PayloadMapper.getAddToCartRequest()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID
      whenever(designDetailRepository.addToCart(USER_ID, expectedRequest)) doReturn getErrorFlow()

      addRequest()
      delay(1000)
      viewModel.addToCart(TYPE)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).addToCart(userIdCaptor.capture(),
          addToCartRequestCaptor.capture())
      assertAddToCartRequest(expectedRequest)
      assertLoading(true)
      delay(1000)

      assertIsAddedToCart(null)
      assertLoading(false)

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @Test
  fun `Given when add to cart and user id is not empty and add to cart request is empty then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn USER_ID

      viewModel.setColorRequest("Green")
      viewModel.addToCart(TYPE)
      delay(1000)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(designDetailRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when add to cart and user id is empty then do nothing`() {
    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.setSizeRequest(2)
      viewModel.addToCart(TYPE)
      delay(1000)

      verify(authSharedPrefRepository).userId

      verifyZeroInteractions(designDetailRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when fetch design detail data then success update live data`() {
    val expectedResponse = PayloadMapper.getDesignDetailResponse()
    val expectedUiModel = PayloadMapper.getDesignDetailUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(BaseMapperModel(expectedResponse, expectedUiModel))

      whenever(designDetailRepository.getDesignDetailById(DESIGN_ID)) doReturn flow
      whenever(savedStateHandle
          .set(DesignDetailViewModel.DESIGN_DETAIL_RESPONSE, expectedResponse)) doAnswer {}
      whenever(savedStateHandle
          .set(DesignDetailViewModel.DESIGN_DETAIL_UI_MODEL, expectedUiModel)) doAnswer {}

      viewModel.fetchDesignDetailData(DESIGN_ID)
      delay(1000)

      verify(designDetailRepository).getDesignDetailById(DESIGN_ID)
      delay(1000)

      viewModel.designDetailResponse.observeForTesting {
        assertEquals(viewModel.designDetailResponse.value, expectedResponse)
        verify(savedStateHandle).set(DesignDetailViewModel.DESIGN_DETAIL_RESPONSE, expectedResponse)
      }
      viewModel.designDetailUiModel.observeForTesting {
        assertEquals(viewModel.designDetailUiModel.value, expectedUiModel)
        verify(savedStateHandle).set(DesignDetailViewModel.DESIGN_DETAIL_UI_MODEL, expectedUiModel)
      }

      verifyNoMoreInteractions(designDetailRepository, savedStateHandle)
    }
  }

  @Test
  fun `Given when fetch design detail data then error and live data not updated`() {
    rule.dispatcher.runBlockingTest {
      whenever(designDetailRepository.getDesignDetailById(DESIGN_ID)) doReturn getErrorFlow()

      viewModel.fetchDesignDetailData(DESIGN_ID)
      delay(1000)

      verify(designDetailRepository).getDesignDetailById(DESIGN_ID)
      delay(1000)

      viewModel.designDetailResponse.observeForTesting {
        assertNull(viewModel.designDetailResponse.value)
      }
      viewModel.designDetailUiModel.observeForTesting {
        assertNull(viewModel.designDetailUiModel.value)
      }
      assertError(Constants.generateFailedFetchError("design"))

      verifyNoMoreInteractions(designDetailRepository)
      verifyZeroInteractions(savedStateHandle)
    }
  }

  private fun addRequest() {
    val designDetail = PayloadMapper.getDesignDetailResponse()
    whenever(authSharedPrefRepository.name) doReturn USER_NAME

    viewModel.setResponse(designDetail)
    viewModel.setAddToCartRequest(designDetail)
    viewModel.setSizeRequest(0)
    viewModel.setColorRequest(designDetail.color[0].name)

    verify(authSharedPrefRepository).name
  }

  private fun assertAddToCartRequest(request: AddToCartRequest) {
    assertEquals(userIdCaptor.firstValue, USER_ID)
    assertEquals(addToCartRequestCaptor.firstValue, request)
  }

  private fun assertIsAddedToCart(wishlistId: String?) {
    viewModel.isAddedToCart.observeForTesting {
      with(viewModel.isAddedToCart.value) {
        assertEquals(this?.first, TYPE)
        assertEquals(this?.second, wishlistId)
      }
    }
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertLoading(loading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, loading)
    }
  }
}