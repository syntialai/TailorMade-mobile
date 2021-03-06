package com.future.tailormade_design_detail.core.mapper

import com.future.tailormade.base.mapper.BaseMapper
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
import com.future.tailormade_design_detail.core.model.response.DesignDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeDetailResponse
import com.future.tailormade_design_detail.core.model.response.SizeResponse
import com.future.tailormade_design_detail.core.model.ui.DesignDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.core.model.ui.SizeUiModel

object DesignDetailMapper {

  fun mapToDesignDetailUiModel(designDetailResponse: DesignDetailResponse) = DesignDetailUiModel(
      id = designDetailResponse.id, title = designDetailResponse.title,
      description = designDetailResponse.description, tailorId = designDetailResponse.tailorId,
      tailorName = designDetailResponse.tailorName, image = designDetailResponse.image,
      price = designDetailResponse.price.toIndonesiaCurrencyFormat(),
      discount = BaseMapper.setDiscount(designDetailResponse.price, designDetailResponse.discount),
      category = designDetailResponse.category, color = designDetailResponse.color,
      size = setSize(designDetailResponse.size))

  fun mapToSizeDetailUiModel(sizeDetailResponse: SizeDetailResponse): SizeDetailUiModel {
    val sizeDetailUiModel = SizeDetailUiModel()
    with(sizeDetailResponse) {
      chest?.let { sizeDetailUiModel.chest = it.toString() + "cm" }
      hips?.let { sizeDetailUiModel.hips = it.toString() + "cm" }
      waist?.let { sizeDetailUiModel.waist = it.toString() + "cm" }
      inseam?.let { sizeDetailUiModel.inseam = it.toString() + "cm" }
      neckToWaist?.let { sizeDetailUiModel.neckToWaist = it.toString() + "cm" }
    }
    return sizeDetailUiModel
  }

  private fun setSize(sizes: List<SizeResponse>): MutableList<SizeUiModel> {
    val sizesUiModel = mutableListOf<SizeUiModel>()
    sizes.forEach { size ->
      val sizeUiModel = SizeUiModel(name = size.name)
      size.detail?.let {
        sizeUiModel.detail = mapToSizeDetailUiModel(size.detail)
      }
      sizesUiModel.add(sizeUiModel)
    }
    return sizesUiModel
  }
}