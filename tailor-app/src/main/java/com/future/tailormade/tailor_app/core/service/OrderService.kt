package com.future.tailormade.tailor_app.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.tailor_app.core.api.TailorAppApiUrl
import com.future.tailormade.tailor_app.core.model.response.order.OrderResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {

  @GET(TailorAppApiUrl.BASE_TAILORS_ID_ORDERS_PATH)
  suspend fun getTailorOrders(@Path("tailorId") tailorId: String, @Query("status") status: String,
      @Query("page") page: Int, @Query("itemPerPage") itemPerPage: Int):
      BaseListResponse<OrderResponse>

  @GET(TailorAppApiUrl.TAILORS_ID_ORDERS_ID_PATH)
  suspend fun getTailorOrderById(
      @Path("tailorId") tailorId: String, @Path("id") id: String):
      BaseSingleObjectResponse<OrderDetailResponse>

  @PUT(TailorAppApiUrl.TAILORS_ID_ORDERS_ID_ACCEPT_PATH)
  suspend fun putAcceptOrder(@Path("tailorId") tailorId: String,
      @Path("id") id: String, @Body body: Any = Object()): BaseResponse

  @PUT(TailorAppApiUrl.TAILORS_ID_ORDERS_ID_REJECT_PATH)
  suspend fun putRejectOrder(@Path("tailorId") tailorId: String,
      @Path("id") id: String, @Body body: Any = Object()): BaseResponse
}