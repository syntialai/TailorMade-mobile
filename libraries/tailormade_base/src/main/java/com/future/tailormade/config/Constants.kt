package com.future.tailormade.config

object Constants {

  /**
   * Static data
   */
  const val MIN_DEBOUNCE_TIME = 300L
  const val DEBOUNCE_TIME_500 = 500L
  const val MIN_PASSWORD_LENGTH = 8
  const val MIN_QUERY_SEARCH_LENGTH = 3
  const val REFRESH_DELAY_TIME = 1000L
  const val INDONESIA_TIME_ZONE = "Asia/Jakarta"
  const val INITIAL_PAGING_PAGE = 1
  const val INITIAL_PAGING_ITEM_PER_PAGE = 10
  const val COMPRESS_IMAGE_QUALITY = 100
  const val TYPE_IMAGE_ALL = "image/*"
  const val TYPE_IMAGE_JPEG = "image/jpeg"
  const val TYPE_IMAGE_PNG = "image/png"
  const val MESSAGES_TYPE_TEXT = "TEXT"

  /*
   * Date Patterns
   */
  const val DD_MMMM_YYYY = "dd MMMM yyyy"
  const val DD_MMM_YYYY = "dd MMM yyyy"
  const val HH_MM = "hh:mm"
  const val DD_MMMM_YYYY_HH_MM_SS = "dd MMMM yyyy, hh:mm:ss"

  /**
   * Error messages
   */
  private const val FAILED_TO = "Failed to"
  private const val IS_EMPTY = "is empty"
  private const val IS_NOT_VALID = "is not valid"

  const val PHONE_NUMBER_IS_EMPTY = "Phone number $IS_EMPTY"
  const val PHONE_NUMBER_IS_NOT_VALID = "Phone number $IS_NOT_VALID"
  const val VERIFICATION_CODE_IS_WRONG = "Verification code is wrong"
  const val SIGN_UP_ERROR = "Failed to sign up!"
  const val SIGN_IN_ERROR = "Please check your email and/or password!"
  const val NAME_IS_EMPTY = "Name $IS_EMPTY"
  const val EMAIL_IS_EMPTY = "Email $IS_EMPTY"
  const val EMAIL_IS_NOT_VALID = "Email $IS_NOT_VALID"
  const val BIRTH_DATE_IS_NOT_SET = "Birth date is not set"
  const val PASSWORD_IS_EMPTY = "Password $IS_EMPTY"
  const val PASSWORD_IS_NOT_VALID = "Password length should be more than 7 characters"
  const val CONFIRM_PASSWORD_IS_EMPTY = "Confirm password $IS_EMPTY"
  const val CONFIRM_PASSWORD_MUST_BE_SAME_WITH_PASSWORD = "Confirm password must be same with password"

  val FAILED_TO_GET_PROFILE_INFO = generateFailedFetchError("profile info")
  val FAILED_TO_UPDATE_PROFILE = generateFailedUpdateError("profile")
  val FAILED_TO_GET_YOUR_CART_ITEM = generateFailedFetchError("your cart item", true)
  val FAILED_TO_UPDATE_YOUR_CART_ITEM = generateFailedUpdateError("your cart item", true)
  val FAILED_TO_GET_CHECKOUT_DATA = generateFailedFetchError("checkout")
  val FAILED_TO_CHECKOUT_ITEM = generateFailedError("checkout", "item", true)
  val FAILED_TO_DELETE_CART_ITEM = generateFailedDeleteError("your cart item", true)

  fun generateFailedFetchError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "get", objectToFetch, isNotData)

  private fun generateFailedUpdateError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "update", objectToFetch, isNotData)

  private fun generateFailedDeleteError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "delete", objectToFetch, isNotData)

  private fun generateFailedError(method: String,
      objectToFetch: String, isNotData: Boolean? = null) = "$FAILED_TO $method $objectToFetch${
    isNotData?.let {
      ""
    } ?: run {
      " data"
    }
  }. Please try again."
}