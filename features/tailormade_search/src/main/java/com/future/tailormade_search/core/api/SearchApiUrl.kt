package com.future.tailormade_search.core.api

import com.future.tailormade.api.ApiUrl

object SearchApiUrl {

  private const val BASE_SEARCH_PATH = ApiUrl.API_PATH + "/search"

  const val SEARCH_DESIGN_BY_QUERY_PATH = "$BASE_SEARCH_PATH/design"
  const val SEARCH_TAILOR_BY_QUERY_PATH = "$BASE_SEARCH_PATH/tailor"
}