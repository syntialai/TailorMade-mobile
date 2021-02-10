package com.future.tailormade_design_detail.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartDesignRequest
import com.future.tailormade_design_detail.core.model.request.cart.AddToCartRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignColorRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignSizeDetailRequest
import com.future.tailormade_design_detail.core.model.request.design.DesignSizeRequest
import com.future.tailormade_design_detail.core.model.response.AddToCartResponse
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse
import com.future.tailormade_design_detail.core.model.ui.DesignDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  private const val DESIGN_DESCRIPTION = "DESCRIPTION"
  private const val WISHLIST_ID = "WISHLIST_ID"

  fun getAddToCartDesignRequest() = AddToCartDesignRequest(id = BaseTest.DESIGN_ID,
      title = BaseTest.DESIGN_TITLE, image = BaseTest.DESIGN_IMAGE,
      discount = BaseTest.DESIGN_DISCOUNT, price = BaseTest.DESIGN_PRICE,
      size = getSizeResponse()[0].name, color = getColorResponse()[0].name,
      sizeDetail = getSizeDetailResponse())

  fun getAddToCartRequest() = AddToCartRequest(userName = BaseTest.USER_NAME,
      tailorId = BaseTest.TAILOR_ID, tailorName = BaseTest.TAILOR_NAME,
      quantity = BaseTest.DESIGN_QUANTITY, design = getAddToCartDesignRequest())

  fun getAddToCartResponse() = AddToCartResponse(wishlistId = WISHLIST_ID)

  fun getDesignDetailRequest() = DesignRequest(title = BaseTest.DESIGN_TITLE,
      image = BaseTest.DESIGN_IMAGE, discount = BaseTest.DESIGN_DISCOUNT,
      price = BaseTest.DESIGN_PRICE, description = DESIGN_DESCRIPTION, size = getSizeRequest(),
      color = getColorRequest(), category = listOf())

  fun getDesignDetailResponse() = DesignDetailResponse(id = BaseTest.DESIGN_ID,
      title = BaseTest.DESIGN_TITLE, image = BaseTest.DESIGN_IMAGE,
      discount = BaseTest.DESIGN_DISCOUNT, price = BaseTest.DESIGN_PRICE,
      tailorId = BaseTest.TAILOR_ID, tailorName = BaseTest.TAILOR_NAME,
      description = DESIGN_DESCRIPTION, size = getSizeResponse(), color = getColorResponse(),
      category = listOf(), active = true)

  fun getDesignDetailUiModel() = DesignDetailUiModel(id = BaseTest.DESIGN_ID,
      title = BaseTest.DESIGN_TITLE, image = BaseTest.DESIGN_IMAGE, discount = null,
      price = BaseTest.DESIGN_PRICE_UI_MODEL, tailorId = BaseTest.TAILOR_ID,
      tailorName = BaseTest.TAILOR_NAME, description = DESIGN_DESCRIPTION, size = getSizeUiModel(),
      color = getColorResponse(), category = listOf())

  fun getColorRequest() = listOf(DesignColorRequest(name = "Blue", color = "#0000FF"),
      DesignColorRequest(name = "Green", color = "#00FF00"))

  fun getColorResponse() = listOf(ColorResponse("Blue", "#0000FF"),
      ColorResponse("Green", "#00FF00"))

  fun getSizeDetailRequest() = DesignSizeDetailRequest(chest = 98.4f, hips = 40f, inseam = 50.0f,
      neckToWaist = 48f, waist = 70.5f)

  fun getSizeDetailResponse() = SizeDetailResponse(chest = 98.4f, hips = 40f, inseam = 50.0f,
      neckToWaist = 48f, waist = 70.5f)

  fun getSizeDetailUiModel() = SizeDetailUiModel(chest = "98.4cm", hips = "40.0cm",
      inseam = "50.0cm", neckToWaist = "48.0cm", waist = "70.5cm")

  fun getSizeRequest() = listOf(DesignSizeRequest("S", getSizeDetailRequest()))

  fun getSizeResponse() = listOf(SizeResponse("S", getSizeDetailResponse()))

  fun getSizeUiModel() = mutableListOf(SizeUiModel("S", getSizeDetailUiModel()))
}