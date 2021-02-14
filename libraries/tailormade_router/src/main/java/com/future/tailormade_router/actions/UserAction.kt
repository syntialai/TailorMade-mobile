package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent
import android.net.Uri

object UserAction {

  /**
   * Param name
   */
  private const val PARAM_CART_ITEM_ID = "PARAM_CART_ITEM_ID"
  private const val PARAM_TAILOR_ID = "PARAM_TAILOR_ID"
  private const val PARAM_TAILOR_NAME = "PARAM_TAILOR_NAME"
  private const val PARAM_SWAP_FACE_IMAGE_SOURCE = "PARAM_SWAP_FACE_IMAGE_SOURCE"
  private const val PARAM_SWAP_FACE_IMAGE_DESTINATION = "PARAM_SWAP_FACE_IMAGE_DESTINATION"

  /**
   * Action name
   */
  private const val ACTION_OPEN_CHECKOUT = "com.future.tailormade.checkout.open"
  private const val ACTION_OPEN_HISTORY = "com.future.tailormade.history.open"
  private const val ACTION_OPEN_MAIN = "com.future.tailormade.main.open"
  private const val ACTION_OPEN_SWAP_FACE = "com.future.tailormade.faceSwap.open"
  private const val ACTION_OPEN_TAILOR_PROFILE = "com.future.tailormade.tailorProfile.open"

  /**
   * Action function
   */
  fun goToCheckout(context: Context, cartItemId: String) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_CHECKOUT).apply {
        putExtra(PARAM_CART_ITEM_ID, cartItemId)
      })

  fun goToHistory(context: Context) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_HISTORY))

  fun goToMain(context: Context) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_MAIN).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      })

  fun goToSwapFace(
      context: Context, bitmapSource: Uri, bitmapDestination: String) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_SWAP_FACE).apply {
        putExtra(PARAM_SWAP_FACE_IMAGE_SOURCE, bitmapSource.toString())
        putExtra(PARAM_SWAP_FACE_IMAGE_DESTINATION, bitmapDestination)
      })

  fun goToTailorProfile(context: Context, tailorId: String, tailorName: String) = context.startActivity(
      Action.getIntent(context, ACTION_OPEN_TAILOR_PROFILE).apply {
        putExtra(PARAM_TAILOR_ID, tailorId)
        putExtra(PARAM_TAILOR_NAME, tailorName)
      })
}