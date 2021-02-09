package com.future.tailormade_search.core.repository

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_search.base.PayloadMapper
import com.future.tailormade_search.core.repository.impl.SearchRepositoryImpl
import com.future.tailormade_search.core.service.SearchService
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
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
class SearchRepositoryImplTest : BaseTest() {

  private lateinit var searchRepository: SearchRepository

  @Mock
  private lateinit var searchService: SearchService

  private val dispatcher = TestCoroutineDispatcher()

  @Before
  override fun setUp() {
    MockitoAnnotations.openMocks(this)
    searchRepository = SearchRepositoryImpl(searchService, dispatcher)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when search design then success return designs response`() {
    val expectedResponse = generateListBaseResponse(
        data = listOf(PayloadMapper.getSearchDesignsResponse()))

    dispatcher.runBlockingTest {
      searchService.stub {
        onBlocking {
          searchDesign(PayloadMapper.DESIGN_QUERY, PAGE, ITEM_PER_PAGE)
        } doReturn expectedResponse

        val flow = searchRepository.searchDesign(PayloadMapper.DESIGN_QUERY, PAGE, ITEM_PER_PAGE)

        flow.collect {
          Mockito.verify(searchService).searchDesign(PayloadMapper.DESIGN_QUERY, PAGE,
              ITEM_PER_PAGE)
          assertEquals(expectedResponse, it)
        }
      }
    }
  }

  @Test
  fun `Given when search tailor then success return designs response`() {
    val expectedResponse = generateListBaseResponse(
        data = listOf(PayloadMapper.getSearchTailorsResponse()))

    dispatcher.runBlockingTest {
      searchService.stub {
        onBlocking {
          searchTailor(PayloadMapper.TAILOR_QUERY, PAGE, ITEM_PER_PAGE)
        } doReturn expectedResponse

        val flow = searchRepository.searchTailor(PayloadMapper.TAILOR_QUERY, PAGE, ITEM_PER_PAGE)

        flow.collect {
          Mockito.verify(searchService).searchTailor(PayloadMapper.TAILOR_QUERY, PAGE,
              ITEM_PER_PAGE)
          assertEquals(expectedResponse, it)
        }
      }
    }
  }
}