package com.future.tailormade_search.core.repository

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

  suspend fun searchDesign(
      query: String, page: Int, itemPerPage: Int): Flow<BaseListResponse<SearchDesignResponse>>

  suspend fun searchTailor(
      query: String, page: Int, itemPerPage: Int): Flow<BaseListResponse<SearchTailorResponse>>
}