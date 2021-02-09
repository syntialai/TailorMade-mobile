package com.future.tailormade_search.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  const val DESIGN_QUERY = "Design"
  const val TAILOR_QUERY = "Tailor"

  fun getSearchDesignsResponse() = SearchDesignResponse(id = BaseTest.DESIGN_ID,
      title = DESIGN_QUERY, price = BaseTest.DESIGN_PRICE, discount = BaseTest.DESIGN_DISCOUNT,
      image = BaseTest.DESIGN_IMAGE)

  fun getSearchTailorsResponse() = SearchTailorResponse(id = BaseTest.TAILOR_ID,
      name = BaseTest.TAILOR_NAME, imagePath = BaseTest.TAILOR_IMAGE, location = null)
}