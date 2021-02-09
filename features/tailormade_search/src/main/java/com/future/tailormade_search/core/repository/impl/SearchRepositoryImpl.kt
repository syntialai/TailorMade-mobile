package com.future.tailormade_search.core.repository.impl

import com.future.tailormade_search.core.repository.SearchRepository
import com.future.tailormade_search.core.service.SearchService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl @Inject constructor(private val searchService: SearchService,
    private val ioDispatcher: CoroutineDispatcher) : SearchRepository {

  override suspend fun searchDesign(query: String, page: Int, itemPerPage: Int) = flow {
    emit(searchService.searchDesign(query, page, itemPerPage))
  }.flowOn(ioDispatcher)

  override suspend fun searchTailor(query: String, page: Int, itemPerPage: Int) = flow {
    emit(searchService.searchTailor(query, page, itemPerPage))
  }.flowOn(ioDispatcher)
}