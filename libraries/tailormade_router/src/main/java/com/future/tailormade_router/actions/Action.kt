package com.future.tailormade_router.actions

import android.content.Context
import android.content.Intent

object Action {

    /*
     * Action name
     */
    private const val ACTION_OPEN_SIGN_IN = "com.future.tailormade.signIn.open"
    private const val ACTION_OPEN_PROFILE = "com.future.tailormade.editProfile.open"
    private const val ACTION_OPEN_SETTINGS = "com.future.tailormade.settings.open"
    private const val ACTION_OPEN_SEARCH = "com.future.tailormade.search.open"

    /*
     * Action function
     */
    fun goToSignIn(context: Context) = getIntent(context, ACTION_OPEN_SIGN_IN)

    fun goToProfile(context: Context) = getIntent(context, ACTION_OPEN_PROFILE)

    fun goToSettings(context: Context) = getIntent(context, ACTION_OPEN_SETTINGS)

    fun goToSearch(context: Context) = getIntent(context, ACTION_OPEN_SEARCH)

    private fun getIntent(context: Context, action: String) =
        Intent(action).setPackage(context.packageName)
}