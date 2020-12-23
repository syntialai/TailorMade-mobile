package com.future.tailormade.core.api

import com.future.tailormade.api.ApiUrl
import com.future.tailormade_auth.core.api.AuthApiUrl

object AppApiUrl {

  /**
   * Dashboard API url
   */
  private const val BASE_DASHBOARD_PATH = ApiUrl.API_PATH + "/dashboard"
  const val DASHBOARD_TAILORS_PATH = "$BASE_DASHBOARD_PATH/tailors"

  /**
   * Cart/Wishlist API url
   */
  const val BASE_WISHLISTS_PATH = ApiUrl.API_PATH + "/wishlists"
  const val WISHLISTS_ID_PATH = "$BASE_WISHLISTS_PATH{id}"
  const val WISHLISTS_ID_EDIT_QUANTITY_PATH = "$WISHLISTS_ID_PATH/_edit-quantity"
  const val WISHLISTS_ID_CHECKOUT_PATH = "$WISHLISTS_ID_PATH/_checkout"

  /**
   * Order API url
   */
  const val USERS_ID_ORDERS_PATH = "${AuthApiUrl.BASE_USERS_PATH}/{userId}/orders"
  const val USERS_ID_ORDERS_ID_PATH = "$USERS_ID_ORDERS_PATH/{id}"
}