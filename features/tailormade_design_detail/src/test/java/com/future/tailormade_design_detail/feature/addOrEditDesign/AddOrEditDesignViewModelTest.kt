package com.future.tailormade_design_detail.feature.addOrEditDesign

import com.future.tailormade.base.model.BaseMapperModel
import com.future.tailormade.config.Constants
import com.future.tailormade_design_detail.base.BaseViewModelTest
import com.future.tailormade_design_detail.base.PayloadMapper
import com.future.tailormade_design_detail.core.model.request.design.DesignRequest
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.feature.addOrEditDesign.viewModel.AddOrEditDesignViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import java.io.File
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
class AddOrEditDesignViewModelTest : BaseViewModelTest() {

  companion object {
    private const val PRICE = DESIGN_PRICE.toString()
    private const val DISCOUNT = DESIGN_DISCOUNT.toString()
  }

  private lateinit var viewModel: AddOrEditDesignViewModel

  private val designDetailRepository = mock<DesignDetailRepository>()

  private val tailorIdCaptor = argumentCaptor<String>()

  private val designIdCaptor = argumentCaptor<String>()

  private val designRequestCaptor = argumentCaptor<DesignRequest>()

  private val imageFileCaptor = argumentCaptor<File>()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    viewModel = AddOrEditDesignViewModel(designDetailRepository, authSharedPrefRepository,
        savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @FlowPreview
  @Test
  fun `Given when add design then success upload image and add design and update live data`() {
    val filePath = "FILE PATH"
    val expectedRequest = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      val uploadImageFlow = getFlow(DESIGN_IMAGE)
      val addDesignFlow = getFlow(expectedResponse)

      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(designDetailRepository.uploadDesignImage(any())) doReturn uploadImageFlow
      whenever(designDetailRepository.addDesignByTailor(any(), any())) doReturn addDesignFlow

      viewModel.setImageFile(filePath)
      setDesign(expectedResponse)
      viewModel.addDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).uploadDesignImage(imageFileCaptor.capture())
      assertEquals(imageFileCaptor.firstValue.path, filePath)
      assertLoading(true)
      delay(1000)

      verify(designDetailRepository).addDesignByTailor(tailorIdCaptor.capture(),
          designRequestCaptor.capture())
      assertDesignRequest(expectedRequest)
      delay(1000)

      assertLoading(false)
      assertDesignResponse(expectedResponse)
      assertIsUpdated(true)

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @FlowPreview
  @Test
  fun `Given when add design and error when add design then success upload image and update live data`() {
    val filePath = "FILE PATH"
    val expectedRequest = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      val uploadImageFlow = getFlow(DESIGN_IMAGE)

      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(designDetailRepository.uploadDesignImage(any())) doReturn uploadImageFlow
      whenever(designDetailRepository.addDesignByTailor(any(), any())) doReturn getErrorFlow()

      viewModel.setImageFile(filePath)
      setDesign(expectedResponse)
      viewModel.addDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).uploadDesignImage(imageFileCaptor.capture())
      assertEquals(imageFileCaptor.firstValue.path, filePath)
      assertLoading(true)
      delay(1000)

      verify(designDetailRepository).addDesignByTailor(tailorIdCaptor.capture(),
          designRequestCaptor.capture())
      assertDesignRequest(expectedRequest)
      delay(1000)

      assertLoading(false)
      assertError(Constants.generateFailedUpdateError("design"))

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @FlowPreview
  @Test
  fun `Given when add design and error when upload design image then update live data`() {
    val filePath = "FILE PATH"
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(designDetailRepository.uploadDesignImage(any())) doReturn getErrorFlow()

      viewModel.setImageFile(filePath)
      setDesign(expectedResponse)
      viewModel.addDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).uploadDesignImage(imageFileCaptor.capture())
      assertEquals(imageFileCaptor.firstValue.path, filePath)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.generateFailedUpdateError("design"))

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @Test
  fun `Given when add design and tailor id is empty then do nothing`() {
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.updateDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verifyZeroInteractions(designDetailRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when update design then success update the design and live data`() {
    val expectedRequest = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = PayloadMapper.getDesignDetailResponse()
    val expectedUiModel = PayloadMapper.getDesignDetailUiModel()

    rule.dispatcher.runBlockingTest {
      val flow = getFlow(BaseMapperModel(expectedResponse, expectedUiModel))

      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(designDetailRepository.updateDesignById(any(), any(), any())) doReturn flow

      setDesign(expectedResponse)
      delay(1000)

      viewModel.updateDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).updateDesignById(tailorIdCaptor.capture(),
          designIdCaptor.capture(), designRequestCaptor.capture())
      assertDesignRequest(expectedRequest, true)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertDesignResponse(expectedResponse)
      assertIsUpdated(true)

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @Test
  fun `Given when update design and error when update then update live data`() {
    val expectedRequest = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn TAILOR_ID
      whenever(designDetailRepository.updateDesignById(any(), any(), any())) doReturn getErrorFlow()

      setDesign(expectedResponse)
      delay(1000)

      viewModel.updateDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verify(designDetailRepository).updateDesignById(tailorIdCaptor.capture(),
          designIdCaptor.capture(), designRequestCaptor.capture())
      assertDesignRequest(expectedRequest, true)
      assertLoading(true)
      delay(1000)

      assertLoading(false)
      assertError(Constants.generateFailedUpdateError("design"))

      verifyNoMoreInteractions(authSharedPrefRepository, designDetailRepository)
    }
  }

  @Test
  fun `Given when update design and tailor id is empty then do nothing`() {
    val expectedResponse = PayloadMapper.getDesignDetailResponse()

    rule.dispatcher.runBlockingTest {
      whenever(authSharedPrefRepository.userId) doReturn null

      viewModel.updateDesign(title = DESIGN_TITLE, price = PRICE, discount = DISCOUNT,
          description = expectedResponse.description)

      verify(authSharedPrefRepository).userId
      verifyZeroInteractions(designDetailRepository)
      verifyNoMoreInteractions(authSharedPrefRepository)
    }
  }

  @Test
  fun `Given when check is price valid then return true`() {
    val price = (10000.0).toString()
    val discount = (0.0).toString()

    val response = viewModel.isPriceValid(price, discount)

    assertTrue(response)
  }

  @Test
  fun `Given when check is price valid then return false`() {
    val price = (10000.0).toString()
    val discount = (20000.0).toString()

    val response = viewModel.isPriceValid(price, discount)

    assertFalse(response)
  }

  @Test
  fun `Given when validate size request empty then return false and set error message`() {
    val response = viewModel.validate()
    assertFalse(response)
    assertError(Constants.SIZE_IS_EMPTY)
  }

  @Test
  fun `Given when validate color request empty then return false and set error message`() {
    viewModel.addSize("Dummy", PayloadMapper.getSizeDetailUiModel())
    viewModel.addSize("Dummys", PayloadMapper.getSizeDetailUiModel())
    viewModel.removeSize("Dummys", PayloadMapper.getSizeDetailUiModel())
    val response = viewModel.validate()
    assertFalse(response)
    assertError(Constants.COLOR_IS_EMPTY)
  }

  @Test
  fun `Given when validate image request empty then return false and set error message`() {
    viewModel.addSize("Dummy", PayloadMapper.getSizeDetailUiModel())
    viewModel.addColor("Color", "#ffffff")
    viewModel.addColor("Colors", "#ffffff")
    viewModel.removeColor("Colors", "#ffffff")
    val response = viewModel.validate()
    assertFalse(response)
    assertError(Constants.IMAGE_MUST_BE_ATTACHED)
  }

//  @Test
//  fun `Given when validate request then return true`() {
//    viewModel.addSize("Dummy", PayloadMapper.getSizeDetailUiModel())
//    viewModel.addColor("Color", "#ffffff")
//    viewModel.setImageFile("/storage/emulated/0/picture/image.jpeg")
//    val response = viewModel.validate()
//    assertTrue(response)
//  }

  private fun assertDesignRequest(expectedRequest: DesignRequest, update: Boolean = false) {
    assertEquals(tailorIdCaptor.firstValue, TAILOR_ID)
    if (update) {
      assertEquals(designIdCaptor.firstValue, DESIGN_ID)
    }
    assertEquals(designRequestCaptor.firstValue, expectedRequest)
  }

  private fun assertDesignResponse(response: DesignDetailResponse) {
    viewModel.designDetailResponse.observeForTesting {
      assertEquals(viewModel.designDetailResponse.value, response)
    }
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertIsUpdated(isUpdated: Boolean) {
    viewModel.isUpdated.observeForTesting {
      assertEquals(viewModel.isUpdated.value, isUpdated)
    }
  }

  private fun assertLoading(isLoading: Boolean) {
    viewModel.isLoading.observeForTesting {
      assertEquals(viewModel.isLoading.value, isLoading)
    }
  }

  private fun setDesign(design: DesignDetailResponse) {
    viewModel.setDesignDetailResponse(design)
    assertDesignResponse(design)
  }
}