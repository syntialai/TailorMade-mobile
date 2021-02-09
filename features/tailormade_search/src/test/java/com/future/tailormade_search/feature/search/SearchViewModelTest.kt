package com.future.tailormade_search.feature.search

import com.future.tailormade.config.Constants
import com.future.tailormade_search.base.BaseViewModelTest
import com.future.tailormade_search.base.PayloadMapper
import com.future.tailormade_search.core.repository.SearchRepository
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SearchViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: SearchViewModel

  private val searchRepository: SearchRepository = mock()

  @Before
  override fun setUp() {
    viewModel = SearchViewModel(searchRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when search design then success update design data and search result count`() {
    val query = PayloadMapper.DESIGN_QUERY
    val expectedResponse = generateListBaseResponse(data = PayloadMapper.getSearchDesignsResponse())

    runBlocking {
      val flow = getFlow(expectedResponse)
      whenever(searchRepository.searchDesign(any(), any(), any())) doReturn flow

      viewModel.searchDesign(query)
      delay(1000)

      verify(searchRepository).searchDesign(query, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.listOfDesigns.observeForTesting {
        assertEquals(viewModel.listOfDesigns.value, expectedResponse.data)
      }
      assertResultCount(TOTAL_ITEM.toLong())

      verifyNoMoreInteractions(searchRepository)
    }
  }

  @Test
  fun `Given when search design then error and data not updated`() {
    val query = PayloadMapper.DESIGN_QUERY

    runBlocking {
      whenever(searchRepository.searchDesign(any(), any(), any())) doReturn getErrorFlow()

      viewModel.searchDesign(query)
      delay(1000)

      verify(searchRepository).searchDesign(query, PAGE, ITEM_PER_PAGE)
      assertResultCount(-1)

      delay(1000)
      assertError(Constants.generateFailedFetchError("design"))

      verifyNoMoreInteractions(searchRepository)
    }
  }

  @Test
  fun `Given when search tailor then success update tailor data and search result count`() {
    val query = PayloadMapper.TAILOR_QUERY
    val expectedResponse = generateListBaseResponse(data = PayloadMapper.getSearchTailorsResponse())

    runBlocking {
      val flow = getFlow(expectedResponse)
      whenever(searchRepository.searchTailor(any(), any(), any())) doReturn flow

      viewModel.searchTailor(query)
      delay(1000)

      verify(searchRepository).searchTailor(query, PAGE, ITEM_PER_PAGE)
      delay(1000)

      viewModel.listOfTailors.observeForTesting {
        assertEquals(viewModel.listOfTailors.value, expectedResponse.data)
      }
      assertResultCount(TOTAL_ITEM.toLong())

      verifyNoMoreInteractions(searchRepository)
    }
  }

  @Test
  fun `Given when search tailor then error and data not updated`() {
    val query = PayloadMapper.TAILOR_QUERY

    runBlocking {
      whenever(searchRepository.searchTailor(any(), any(), any())) doReturn getErrorFlow()

      viewModel.searchTailor(query)
      delay(1000)

      verify(searchRepository).searchTailor(query, PAGE, ITEM_PER_PAGE)
      assertResultCount(-1)

      delay(1000)
      assertError(Constants.generateFailedFetchError("tailor"))

      verifyNoMoreInteractions(searchRepository)
    }
  }

  @Test
  fun `Given when check valid query then return true`() {
    val query = "Query"
    val response = viewModel.isQueryValid(query)

    assertTrue(response)
  }

  @Test
  fun `Given when check blank query then return false`() {
    val query = ""
    val response = viewModel.isQueryValid(query)

    assertFalse(response)
  }

  @Test
  fun `Given when check query with length less than 3 then return false`() {
    val query = "aa"
    val response = viewModel.isQueryValid(query)

    assertFalse(response)
  }

  private fun assertError(message: String) {
    viewModel.errorMessage.observeForTesting {
      assertEquals(viewModel.errorMessage.value, message)
    }
  }

  private fun assertResultCount(count: Long) {
    viewModel.searchResultCount.observeForTesting {
      assertEquals(viewModel.searchResultCount.value, count)
    }
  }
}