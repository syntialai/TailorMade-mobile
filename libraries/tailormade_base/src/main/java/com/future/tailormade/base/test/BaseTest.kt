package com.future.tailormade.base.test

import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum
import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BasePagingResponse
import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.config.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseTest {

  companion object {
    const val CODE_OK = 200
    const val STATUS_OK = "OK"
    const val CODE_BAD_REQUEST = 400
    const val STATUS_BAD_REQUEST = "BAD_REQUEST"
    const val CODE_UNAUTHORIZED = 401
    const val STATUS_UNAUTHORIZED = "UNAUTHORIZED"
    const val CODE_NOT_FOUND = 404
    const val STATUS_NOT_FOUND = "NOT_FOUND"

    const val PAGE = Constants.INITIAL_PAGING_PAGE
    const val ITEM_PER_PAGE = Constants.INITIAL_PAGING_ITEM_PER_PAGE

    const val USER_ID = "USER ID"
    const val USER_NAME = "USER NAME"
    const val USER_EMAIL = "user@mail.com"
    const val USER_BIRTHDATE = 161188911L
    const val USER_PASSWORD = "userPassword"
    val USER_ROLE = RoleEnum.ROLE_USER
    const val USER_ROLE_ORDINAL = 0
    val USER_GENDER = GenderEnum.Anonymous
    const val USER_GENDER_ORDINAL = 2

    const val TAILOR_ID = "TAILOR ID"
    const val TAILOR_NAME = "TAILOR NAME"
    const val TAILOR_IMAGE = "TAILOR IMAGE"

    const val DESIGN_ID = "DESIGN ID"
    const val DESIGN_TITLE = "DESIGN TITLE"
    const val DESIGN_PRICE = 50000.0
    const val DESIGN_DISCOUNT = 0.0
    const val DESIGN_IMAGE = "IMAGE"
  }

  abstract fun setUp()

  abstract fun tearDown()

  protected fun <T> generateListBaseResponse(code: Int? = null, status: String? = null,
      data: List<T>): BaseListResponse<T> {
    val pagingResponse = BasePagingResponse(Constants.INITIAL_PAGING_PAGE,
        Constants.INITIAL_PAGING_PAGE, 1, 1)
    return BaseListResponse(data, pagingResponse).apply {
      this.code = code
      this.status = status
    }
  }

  protected fun <T> generateSingleBaseResponse(
      code: Int? = null, status: String? = null, data: T) = BaseSingleObjectResponse(data).apply {
    this.code = code
    this.status = status
  }

  protected fun generateBaseResponse(code: Int, status: String) = BaseResponse().apply {
    this.code = code
    this.status = status
  }
}