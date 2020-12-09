package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object Action {

  /**
   * Param name
   */
  private const val PARAM_DESIGN_DETAIL_ID = "DESIGN_DETAIL_ID"

  /**
   * Action name
   */
  private const val ACTION_OPEN_SIGN_IN = "com.future.tailormade.signIn.open"
  private const val ACTION_OPEN_PROFILE = "com.future.tailormade.editProfile.open"
  private const val ACTION_OPEN_SETTINGS = "com.future.tailormade.settings.open"
  private const val ACTION_OPEN_DESIGN_DETAIL = "com.future.tailormade.designDetail.open"

  /**
   * Action function
   */
  fun goToSignIn(context: Context) = getIntent(context, ACTION_OPEN_SIGN_IN)

  fun goToProfile(context: Context) = getIntent(context, ACTION_OPEN_PROFILE)

  fun goToSettings(context: Context) = getIntent(context, ACTION_OPEN_SETTINGS)

  fun goToDesignDetail(context: Context, id: String) = getIntent(context, ACTION_OPEN_DESIGN_DETAIL).apply {
    putExtra(PARAM_DESIGN_DETAIL_ID, id)
  }

  private fun getIntent(context: Context, action: String) = Intent(action).setPackage(
      context.packageName)
}