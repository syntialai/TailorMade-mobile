package com.future.tailormade.config

object Constants {

  /**
   * Static data
   */
  const val MIN_DEBOUNCE_TIME = 300L
  const val MIN_PASSWORD_LENGTH = 8
  const val MIN_QUERY_SEARCH_LENGTH = 3
  const val MIN_PHONE_NUMBER_LENGTH = 10
  const val REFRESH_DELAY_TIME = 600L
  const val INITIAL_PAGING_PAGE = 0
  const val INITIAL_PAGING_ITEM_PER_PAGE = 20
  const val COMPRESS_IMAGE_QUALITY = 100
  const val TYPE_IMAGE_ALL = "image/*"
  const val TYPE_IMAGE_JPEG = "image/jpeg"
  const val MESSAGES_TYPE_TEXT = "TEXT"
  const val STATUS_ACCEPTED = "Accepted"
  const val STATUS_REJECTED = "Rejected"
  const val UPLOAD_TYPE_DESIGN = "design"

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
  const val SIGN_UP_ERROR = "Failed to sign up!"
  const val SIGN_IN_ERROR = "Please check your email and/or password!"
  const val IMAGE_MUST_BE_ATTACHED = "Image must be attached"
  const val SIZE_IS_EMPTY = "Size is empty"
  const val COLOR_IS_EMPTY = "Color is empty"

  val FAILED_TO_GET_PROFILE_INFO = generateFailedFetchError("profile info")
  val FAILED_TO_UPDATE_PROFILE = generateFailedUpdateError("profile")
  val FAILED_TO_GET_YOUR_CART_ITEM = generateFailedFetchError("your cart item", true)
  val FAILED_TO_UPDATE_YOUR_CART_ITEM = generateFailedUpdateError("your cart item", true)
  val FAILED_TO_GET_CHECKOUT_DATA = generateFailedFetchError("checkout")
  val FAILED_TO_CHECKOUT_ITEM = generateFailedError("checkout", "item", true)
  val FAILED_TO_DELETE_CART_ITEM = generateFailedDeleteError("your cart item", true)

  val FAILED_TO_DELETE_DESIGN = generateFailedDeleteError("design")
  val FAILED_TO_GET_YOUR_DESIGN = generateFailedFetchError("your designs")
  val FAILED_TO_ACCEPT_ORDER = generateFailedError("accept", "order", true)
  val FAILED_TO_REJECT_ORDER = generateFailedError("reject", "order", true)
  val FAILED_TO_FETCH_INCOMING_ORDER = generateFailedFetchError("incoming orders", true)
  val FAILED_TO_FETCH_RECENT_ORDER = generateFailedFetchError("recent orders", true)
  val FAILED_TO_FETCH_ORDER_DETAIL = generateFailedFetchError("order detail")

  fun generateFailedAddError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "add", objectToFetch, isNotData)

  fun generateFailedFetchError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "get", objectToFetch, isNotData)

  fun generateFailedUpdateError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "update", objectToFetch, isNotData)

  fun generateFailedDeleteError(objectToFetch: String, isNotData: Boolean? = null) = generateFailedError(
      "delete", objectToFetch, isNotData)

  private fun generateFailedError(method: String,
      objectToFetch: String, isNotData: Boolean? = null) = "Failed to $method $objectToFetch${
    isNotData?.let {
      ""
    } ?: run {
      " data"
    }
  }. Please try again."
}