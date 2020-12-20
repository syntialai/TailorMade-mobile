package com.future.tailormade_search.core.repository.impl

import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_search.core.repository.SearchRepository
import com.future.tailormade_search.core.service.SearchService
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService) : SearchRepository {

  override suspend fun searchDesign(query: String) = flow {
    emit(searchService.searchDesign(query))
  }.flowOnIO()

  override suspend fun searchTailor(query: String) = flow {
    emit(searchService.searchTailor(query))
  }.flowOnIO()
}