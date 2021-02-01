package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object TailorAction {

  /**
   * Param name
   */
  private const val PARAM_ORDER_DETAIL_ID = "ORDER_DETAIL_ID"
  private const val PARAM_DESIGN_DETAIL_ADD = "PARAM_DESIGN_DETAIL_ADD"

  /**
   * Action name
   */
  private const val ACTION_OPEN_ORDER_DETAIL = "com.future.tailormade.orderDetail.open"
  private const val ACTION_OPEN_MAIN = "com.future.tailormade.main.open"
  private const val ACTION_OPEN_DESIGN_DETAIL = "com.future.tailormade.designDetail.open"

  /**
   * Action function
   */
  fun goToOrderDetail(context: Context, orderDetailId: String) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_ORDER_DETAIL).apply {
        putExtra(PARAM_ORDER_DETAIL_ID, orderDetailId)
      })

  fun goToMain(context: Context) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_MAIN).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      })

  fun goToAddDesignDetail(context: Context) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_DESIGN_DETAIL).apply {
        putExtra(PARAM_DESIGN_DETAIL_ADD, true)
      })
}