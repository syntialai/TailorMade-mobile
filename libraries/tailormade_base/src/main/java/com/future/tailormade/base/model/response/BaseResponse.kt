package com.future.tailormade.base.model.response

import com.google.gson.annotations.Expose

open class BaseResponse {

  @Expose
  val code: Int? = null

  @Expose
  val status: String? = null
}