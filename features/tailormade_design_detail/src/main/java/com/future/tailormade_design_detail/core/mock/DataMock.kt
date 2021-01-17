package com.future.tailormade_design_detail.core.mock

import com.future.tailormade_design_detail.core.model.response.ColorResponse
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse

object DataMock {

  private const val USER_ID = "USER_ID"
  private const val USER_NAME = "USER_NAME"
  private const val TAILOR_ID = "TAILOR_ID"
  private const val TAILOR_NAME = "TAILOR_NAME"
  private const val DESIGN_ID = "DESIGN_ID"
  private const val DISCOUNT = 0.0
  private const val PRICE = 50000.0

  private val sizeDetailMock = SizeDetailResponse(chest = 98.4f, hips = 40f, inseam = 50.0f,
      neckToWaist = 48f, waist = 70.5f)
  private val sizeDetailMock2 = SizeDetailResponse(hips = 98.4f, chest = 40f, neckToWaist = 50.0f,
      waist = 48f, inseam = 70.5f)
  private val sizeMock = listOf(SizeResponse("S", sizeDetailMock),
      SizeResponse("XS", sizeDetailMock2))

  private val colorMock = listOf(ColorResponse("Blue", "#0000FF"),
      ColorResponse("Green", "#00FF00"))

  fun getDesignDetailMock() = DesignDetailResponse(id = DESIGN_ID, title = "Design 1",
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      discount = DISCOUNT, price = PRICE, tailorId = TAILOR_ID, tailorName = TAILOR_NAME,
      description = "This is design mock only :). please understand me and okokoko byeeeeeeeeee aehahrehaj djkabcuia",
      size = sizeMock, color = colorMock, category = listOf(), active = true)
}