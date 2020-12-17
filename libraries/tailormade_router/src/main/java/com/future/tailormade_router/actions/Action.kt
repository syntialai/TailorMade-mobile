package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object Action {

	/**
	 * Param name
	 */
	private const val PARAM_CHAT_ROOM_ID = "PARAM_CHAT_ROOM_ID"
	private const val PARAM_CART_ITEM_ID = "PARAM_CART_ITEM_ID"

	/**
	 * Action name
	 */
	private const val ACTION_OPEN_SIGN_IN = "com.future.tailormade.signIn.open"
	private const val ACTION_OPEN_PROFILE = "com.future.tailormade.editProfile.open"
	private const val ACTION_OPEN_SETTINGS = "com.future.tailormade.settings.open"
	private const val ACTION_OPEN_SEARCH = "com.future.tailormade.search.open"
	private const val ACTION_OPEN_CHAT_ROOM = "com.future.tailormade.chatRoom.open"
	private const val ACTION_OPEN_CHECKOUT = "com.future.tailormade.checkout.open"

	/**
	 * Action function
	 */
	fun goToSignIn(context: Context) = getIntent(context, ACTION_OPEN_SIGN_IN)

	fun goToProfile(context: Context) = getIntent(context, ACTION_OPEN_PROFILE)

	fun goToSettings(context: Context) = getIntent(context, ACTION_OPEN_SETTINGS)

	fun goToSearch(context: Context) = getIntent(context, ACTION_OPEN_SEARCH)

	fun goToChatRoom(context: Context, chatRoomId: String) = getIntent(context, ACTION_OPEN_CHAT_ROOM).apply {
		putExtra(PARAM_CHAT_ROOM_ID, chatRoomId)
	}

	fun goToCheckout(context: Context, cartItemId: String) = getIntent(context, ACTION_OPEN_CHECKOUT).apply {
		putExtra(PARAM_CART_ITEM_ID, cartItemId)
	}

	private fun getIntent(context: Context, action: String) = Intent(action).setPackage(
			context.packageName)
}