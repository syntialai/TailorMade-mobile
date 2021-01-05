package com.future.tailormade.base.test

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BasePagingResponse
import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.util.extension.flowOnIO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow

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
  }

  abstract fun setUp()

  abstract fun tearDown()

  protected fun <T> getFlowResponse(data: T) = flow {
    emit(data)
  }.flowOnIO()

  protected fun <T> generateListBaseResponse(code: Int? = null, status: String? = null,
      data: List<T>): BaseListResponse<T> {
    val pagingResponse = BasePagingResponse(1, 10, 20)
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