package com.future.tailormade.config

object Constants {

  /**
   * Static data
   */
  const val MIN_DEBOUNCE_TIME = 300L
  const val DEBOUNCE_TIME_500 = 500L
  const val MIN_PASSWORD_LENGTH = 8
  const val MIN_QUERY_SEARCH_LENGTH = 3
  const val REFRESH_DELAY_TIME = 600L
  const val INDONESIA_TIME_ZONE = "Asia/Jakarta"
  const val INITIAL_PAGING_PAGE = 0
  const val INITIAL_PAGING_ITEM_PER_PAGE = 20
  const val COMPRESS_IMAGE_QUALITY = 100
  const val TYPE_IMAGE_ALL = "image/*"
  const val TYPE_IMAGE_JPEG = "image/jpeg"
  const val TYPE_IMAGE_PNG = "image/png"
  const val MESSAGES_TYPE_TEXT = "TEXT"
  const val STATUS_ACCEPTED = "Accepted"
  const val STATUS_REJECTED = "Rejected"

  /*
   * Date Patterns
   */
  const val DD_MMMM_YYYY = "dd MMMM yyyy"
  const val DD_MMM_YYYY = "dd MMM yyyy"
  const val HH_MM = "hh:mm"
  const val DD_MMMM_YYYY_HH_MM_SS = "dd MMMM yyyy, hh:mm:ss"
  const val YYYY_MM_DD = "yyyy-MM-dd"

  /**
   * Error messages
   */
  private const val IS_EMPTY = "is empty"

  const val SIGN_UP_ERROR = "Failed to sign up!"
  const val SIGN_IN_ERROR = "Please check your email and/or password!"
  const val COLOR_NAME_IS_EMPTY = "Color Name $IS_EMPTY"
  const val SIZE_NAME_IS_EMPTY = "Size Name $IS_EMPTY"
  const val CHEST_SIZE_IS_EMPTY = "Chest Size $IS_EMPTY"
  const val WAIST_SIZE_IS_EMPTY = "Waist Size $IS_EMPTY"
  const val HIPS_SIZE_IS_EMPTY = "Hips Size $IS_EMPTY"
  const val NECK_TO_WAIST_SIZE_IS_EMPTY = "Neck to Waist Size $IS_EMPTY"
  const val INSEAM_SIZE_IS_EMPTY = "Inseam Size $IS_EMPTY"
  const val IMAGE_MUST_BE_ATTACHED = "Image must be attached"
  const val SIZE_IS_EMPTY = "Size $IS_EMPTY"
  const val COLOR_IS_EMPTY = "Color $IS_EMPTY"
  const val DESIGN_NAME_IS_EMPTY = "Design name $IS_EMPTY"
  const val DESIGN_PRICE_IS_EMPTY = "Design price $IS_EMPTY"
  const val DESIGN_DISCOUNT_IS_EMPTY = "Design discount $IS_EMPTY"
  const val DESIGN_DISCOUNT_CANT_BE_HIGHER_THAN_PRICE = "Design discount can't be higher than the normal price"
  const val DESIGN_DESCRIPTION_IS_EMPTY = "Design description $IS_EMPTY"
  const val FAILED_TO_ACCEPT_ORDER = "Failed to accept order. Please try again later."
  const val FAILED_TO_REJECT_ORDER = "Failed to reject order. Please try again later."
  const val FAILED_TO_FETCH_INCOMING_ORDER = "Failed to fetch incoming orders. Please try again later."
  const val FAILED_TO_FETCH_RECENT_ORDER = "Failed to fetch recent orders. Please try again later."
  const val FAILED_TO_FETCH_ORDER_DETAIL = "Failed to fetch order detail. Please try again later."

  val FAILED_TO_GET_PROFILE_INFO = generateFailedFetchError("profile info")
  val FAILED_TO_UPDATE_PROFILE = generateFailedUpdateError("profile")
  val FAILED_TO_GET_YOUR_CART_ITEM = generateFailedFetchError("your cart item", true)
  val FAILED_TO_UPDATE_YOUR_CART_ITEM = generateFailedUpdateError("your cart item", true)
  val FAILED_TO_GET_CHECKOUT_DATA = generateFailedFetchError("checkout")
  val FAILED_TO_CHECKOUT_ITEM = generateFailedError("checkout", "item", true)
  val FAILED_TO_DELETE_CART_ITEM = generateFailedDeleteError("your cart item", true)

  val FAILED_TO_DELETE_DESIGN = generateFailedDeleteError("design")
  val FAILED_TO_GET_YOUR_DESIGN = generateFailedFetchError("your designs")

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