package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.response.cart.CartEditQuantityResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService {

  @GET(AppApiUrl.USERS_ID_WISHLISTS_PATH)
  fun getCarts(@Path("userId") userId: String): BaseListResponse<CartResponse>

  @GET(AppApiUrl.USERS_ID_WISHLISTS_ID_PATH)
  fun getCartById(@Path("userId") userId: String,
      @Path("id") id: String): BaseSingleObjectResponse<CartResponse>

  @PUT(AppApiUrl.USERS_ID_WISHLISTS_ID_EDIT_QUANTITY_PATH) fun putEditCartItemQuantity(
      @Path("userId") userId: String, @Path("id") id: String,
      @Body cartEditQuantityRequest: CartEditQuantityRequest):
      BaseSingleObjectResponse<CartEditQuantityResponse>

  @DELETE(AppApiUrl.USERS_ID_WISHLISTS_ID_PATH)
  fun deleteCartItemById(
      @Path("userId") userId: String, @Path("id") id: String): BaseResponse
}