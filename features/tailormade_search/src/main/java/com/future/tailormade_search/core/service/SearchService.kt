package com.future.tailormade_search.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade_search.core.api.SearchApiUrl
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService {

  @GET(SearchApiUrl.SEARCH_DESIGN_BY_QUERY_PATH)
  suspend fun searchDesign(@Path("query") query: String): BaseListResponse<SearchDesignResponse>

  @GET(SearchApiUrl.SEARCH_TAILOR_BY_QUERY_PATH)
  suspend fun searchTailor(@Path("query") query: String): BaseListResponse<SearchTailorResponse>
}