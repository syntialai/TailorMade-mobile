package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object Action {

  /**
   * Param name
   */
  private const val PARAM_CHAT_ROOM_USER_ID = "PARAM_CHAT_ROOM_USER_ID"
  private const val PARAM_CHAT_ROOM_USER_NAME = "PARAM_CHAT_ROOM_USER_NAME"
  private const val PARAM_DESIGN_DETAIL_ID = "PARAM_DESIGN_DETAIL_ID"
  private const val PARAM_EDIT_PROFILE_TYPE = "PARAM_EDIT_PROFILE_TYPE"

  /**
   * Action name
   */
  private const val ACTION_OPEN_SIGN_IN = "com.future.tailormade.signIn.open"
  private const val ACTION_OPEN_EDIT_PROFILE = "com.future.tailormade.editProfile.open"
  private const val ACTION_OPEN_SETTINGS = "com.future.tailormade.settings.open"
  private const val ACTION_OPEN_SEARCH = "com.future.tailormade.search.open"
  private const val ACTION_OPEN_CHAT_ROOM = "com.future.tailormade.chatRoom.open"
  private const val ACTION_OPEN_DESIGN_DETAIL = "com.future.tailormade.designDetail.open"

  /**
   * Action function
   */
  fun goToSignIn(context: Context) = context.startActivity(
      getIntent(context, ACTION_OPEN_SIGN_IN).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      })

  fun goToEditProfile(context: Context, type: String) = context.startActivity(
      getIntent(context, ACTION_OPEN_EDIT_PROFILE).apply {
        putExtra(PARAM_EDIT_PROFILE_TYPE, type)
      })

  fun goToSettings(context: Context) = context.startActivity(
      getIntent(context, ACTION_OPEN_SETTINGS))

  fun goToSearch(context: Context) = context.startActivity(getIntent(context, ACTION_OPEN_SEARCH))

  fun goToChatRoom(context: Context, userId: String, userName: String) = context.startActivity(
      getIntent(context, ACTION_OPEN_CHAT_ROOM).apply {
        putExtra(PARAM_CHAT_ROOM_USER_ID, userId)
        putExtra(PARAM_CHAT_ROOM_USER_NAME, userName)
      })

  fun goToDesignDetail(context: Context, id: String? = null) = context.startActivity(
      getIntent(context, ACTION_OPEN_DESIGN_DETAIL).apply {
        id?.let { putExtra(PARAM_DESIGN_DETAIL_ID, it) }
      })

  fun getIntent(context: Context, action: String) = Intent(action).setPackage(context.packageName)
}