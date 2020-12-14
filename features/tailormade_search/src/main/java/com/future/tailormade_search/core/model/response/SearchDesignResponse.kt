package com.future.tailormade_search.core.model.response

data class SearchDesignResponse(

  val id: String,

  val title: String,

  val price: Double,

  val discount: Double,

  val imagePath: String
)