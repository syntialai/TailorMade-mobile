package com.future.tailormade_router.actions

import android.content.Context

object TailorAction {

  /**
   * Param name
   */
  private const val PARAM_ORDER_DETAIL_ID = "ORDER_DETAIL_ID"

  /**
   * Action name
   */
  private const val ACTION_OPEN_ORDER_DETAIL = "com.future.tailormade.orderDetail.open"
  private const val ACTION_OPEN_MAIN = "android.intent.action.MAIN"

  /**
   * Action function
   */
  fun goToOrderDetail(context: Context, orderDetailId: String) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_ORDER_DETAIL).apply {
        putExtra(PARAM_ORDER_DETAIL_ID, orderDetailId)
      })

  fun goToMain(context: Context) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_MAIN))
}