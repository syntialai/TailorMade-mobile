package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object Action {

    /**
     * Action name
     */
    private const val ACTION_OPEN_SIGN_IN = "com.future.tailormade.signIn.open"
    private const val ACTION_OPEN_PROFILE = "com.future.tailormade.editProfile.open"
    private const val ACTION_OPEN_SETTINGS = "com.future.tailormade.settings.open"
    private const val ACTION_OPEN_CHAT_ROOM = "com.future.tailormade.chatRoom.open"

    /**
     * Param name
     */
    private const val PARAM_CHAT_ROOM_ID = "PARAM_CHAT_ROOM_ID"

    /**
     * Action function
     */
    fun goToSignIn(context: Context) = getIntent(context, ACTION_OPEN_SIGN_IN)

    fun goToProfile(context: Context) = getIntent(context, ACTION_OPEN_PROFILE)

    fun goToSettings(context: Context) = getIntent(context, ACTION_OPEN_SETTINGS)

    fun goToChatRoom(context: Context, chatRoomId: String) = getIntent(context, ACTION_OPEN_CHAT_ROOM).apply {
        putExtra(PARAM_CHAT_ROOM_ID, chatRoomId)
    }

    private fun getIntent(context: Context, action: String) =
        Intent(action).setPackage(context.packageName)
}