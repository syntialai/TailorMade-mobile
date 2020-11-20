package com.future.tailormade_search.core.service

import com.future.tailormade_search.core.api.SearchApiUrl
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService {

  @GET(SearchApiUrl.SEARCH_DESIGN_BY_QUERY_PATH)
  fun searchDesign(@Path("query") query: String)

  @GET(SearchApiUrl.SEARCH_TAILOR_BY_QUERY_PATH)
  fun searchTailor(@Path("query") query: String)
}