package com.future.tailormade_design_detail.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_design_detail.base.PayloadMapper
import com.future.tailormade_design_detail.core.model.response.UploadImageResponse
import com.future.tailormade_design_detail.core.repository.impl.DesignDetailRepositoryImpl
import com.future.tailormade_design_detail.core.service.DesignDetailService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import java.io.File
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
class DesignDetailRepositoryImplTest : BaseTest() {

  private lateinit var designDetailRepository: DesignDetailRepository

  @Mock
  private lateinit var designDetailService: DesignDetailService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    designDetailRepository = DesignDetailRepositoryImpl(designDetailService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get design detail by id then success return design detail mapped response`() {
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getDesignDetailResponse())
    val expectedUiModel = PayloadMapper.getDesignDetailUiModel()

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { getDesignDetailById(DESIGN_ID) } doReturn expectedResponse
      }

      val flow = designDetailRepository.getDesignDetailById(DESIGN_ID)

      flow.collect {
        verify(designDetailService).getDesignDetailById(DESIGN_ID)
        assertEquals(it.response, expectedResponse.data)
        assertEquals(it.uiModel, expectedUiModel)
      }
    }
  }

  @Test
  fun `Given when get design detail by id and data returned is null then do nothing`() {
    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { getDesignDetailById(DESIGN_ID) } doReturn generateSingleBaseResponse()
      }

      val flow = designDetailRepository.getDesignDetailById(DESIGN_ID)

      flow.collect {
        verify(designDetailService).getDesignDetailById(DESIGN_ID)
      }
    }
  }

  @Test
  fun `Given when add design by tailor then success return design detail response`() {
    val request = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = generateSingleBaseResponse(
        data = PayloadMapper.getDesignDetailResponse())

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { postAddDesignByTailor(TAILOR_ID, request) } doReturn expectedResponse
      }

      val flow = designDetailRepository.addDesignByTailor(TAILOR_ID, request)

      flow.collect {
        verify(designDetailService).postAddDesignByTailor(TAILOR_ID, request)
        assertEquals(it, expectedResponse.data)
      }
    }
  }

  @Test
  fun `Given when add design by tailor and data returned is null then do nothing`() {
    val request = PayloadMapper.getDesignDetailRequest()

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking {
          postAddDesignByTailor(TAILOR_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = designDetailRepository.addDesignByTailor(TAILOR_ID, request)

      flow.collect {
        verify(designDetailService).postAddDesignByTailor(TAILOR_ID, request)
      }
    }
  }

  @Test
  fun `Given when add cart by user then success return design detail response`() {
    val request = PayloadMapper.getAddToCartRequest()
    val expectedResponse = generateSingleBaseResponse(data = PayloadMapper.getAddToCartResponse())

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { postAddToCart(USER_ID, request) } doReturn expectedResponse
      }

      val flow = designDetailRepository.addToCart(USER_ID, request)

      flow.collect {
        verify(designDetailService).postAddToCart(USER_ID, request)
        assertEquals(it, expectedResponse.data)
      }
    }
  }

  @Test
  fun `Given when add cart by user and data returned is null then do nothing`() {
    val request = PayloadMapper.getAddToCartRequest()

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking {
          postAddToCart(USER_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = designDetailRepository.addToCart(USER_ID, request)

      flow.collect {
        verify(designDetailService).postAddToCart(USER_ID, request)
      }
    }
  }

  @Test
  fun `Given when update design by id then success return design detail response`() {
    val request = PayloadMapper.getDesignDetailRequest()
    val expectedResponse = generateSingleBaseResponse(
        data = PayloadMapper.getDesignDetailResponse())
    val expectedUiModel = PayloadMapper.getDesignDetailUiModel()

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { putEditDesignByTailorAndById(TAILOR_ID, DESIGN_ID, request) } doReturn expectedResponse
      }

      val flow = designDetailRepository.updateDesignById(TAILOR_ID, DESIGN_ID, request)

      flow.collect {
        verify(designDetailService).putEditDesignByTailorAndById(TAILOR_ID, DESIGN_ID, request)
        assertEquals(it.response, expectedResponse.data)
        assertEquals(it.uiModel, expectedUiModel)
      }
    }
  }

  @Test
  fun `Given when update design by id and data returned is null then do nothing`() {
    val request = PayloadMapper.getDesignDetailRequest()

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking {
          putEditDesignByTailorAndById(TAILOR_ID, DESIGN_ID, request)
        } doReturn generateSingleBaseResponse()
      }

      val flow = designDetailRepository.updateDesignById(TAILOR_ID, DESIGN_ID, request)

      flow.collect {
        verify(designDetailService).putEditDesignByTailorAndById(TAILOR_ID, DESIGN_ID, request)
      }
    }
  }

  @Test
  fun `Given when upload design image then success return design detail response`() {
    val file = getFileMock(true)
    val expectedResponse = generateSingleBaseResponse(data = UploadImageResponse(DESIGN_IMAGE))

    dispatcher.runBlockingTest {
      designDetailService.stub {
        onBlocking { postUploadImage(any(), any()) } doReturn expectedResponse
      }

      val flow = designDetailRepository.uploadDesignImage(file)

      flow.collect {
        verify(designDetailService).postUploadImage(any(), any())
        assertEquals(it, DESIGN_IMAGE)
      }
    }
  }

  @Test
  fun `Given when upload design image and file is empty then return blank string data`() {
    val file = getFileMock(false)

    dispatcher.runBlockingTest {
      val flow = designDetailRepository.uploadDesignImage(file)

      flow.collect {
        verifyZeroInteractions(designDetailService)
        assertEquals(it, "")
      }
    }
  }

  private fun getFileMock(isExist: Boolean) = mock<File>().stub {
    on { exists() } doReturn isExist
    on { name } doReturn "FILE NAME"
  }
}