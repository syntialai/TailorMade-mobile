package com.future.tailormade.base.repository

import android.content.Context
import android.content.SharedPreferences
import com.future.tailormade.base.model.enums.GenderEnum
import com.future.tailormade.base.model.enums.RoleEnum

class AuthSharedPrefRepository private constructor(context: Context) {

  private val sharedPreferences: SharedPreferences

  companion object {
    private const val SHARED_PREFERENCES_NAME = "Authentication"
    private const val ACCESS_TOKEN = "accessToken"
    private const val REFRESH_TOKEN = "refreshToken"
    private const val USER_ID = "userId"
    private const val USER_NAME = "userName"
    private const val USER_USERNAME = "userUsername"
    private const val USER_ROLE = "userRole"
    private const val USER_GENDER = "userGender"
    private var instance: AuthSharedPrefRepository? = null

    fun newInstance(context: Context) = AuthSharedPrefRepository(context)
  }

  init {
    sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
  }

  fun clearSharedPrefs() {
    val role = userRole
    sharedPreferences.edit().clear().apply()
    userRole = role
  }

  var accessToken: String?
    get() = sharedPreferences.getString(ACCESS_TOKEN, null)
    set(accessToken) {
      sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply()
    }

  var refreshToken: String?
    get() = sharedPreferences.getString(REFRESH_TOKEN, null)
    set(refreshToken) {
      sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

  var userId: String?
    get() = sharedPreferences.getString(USER_ID, null)
    set(userId) {
      sharedPreferences.edit().putString(USER_ID, userId).apply()
    }

  var name: String
    get() = sharedPreferences.getString(USER_NAME, "").orEmpty()
    set(name) {
      sharedPreferences.edit().putString(USER_NAME, name).apply()
    }

  var username: String?
    get() = sharedPreferences.getString(USER_USERNAME, null)
    set(username) {
      sharedPreferences.edit().putString(USER_USERNAME, username).apply()
    }

  var userRole: Int
    get() = sharedPreferences.getInt(USER_ROLE, RoleEnum.ROLE_USER.ordinal)
    set(userRole) {
      sharedPreferences.edit().putInt(USER_ROLE, userRole).apply()
    }

  var userGender: Int
    get() = sharedPreferences.getInt(USER_GENDER, GenderEnum.Anonymous.ordinal)
    set(userGender) {
      sharedPreferences.edit().putInt(USER_GENDER, userGender).apply()
    }

  fun isUser() = userRole == RoleEnum.ROLE_USER.ordinal

  fun isTailor() = userRole == RoleEnum.ROLE_TAILOR.ordinal
}