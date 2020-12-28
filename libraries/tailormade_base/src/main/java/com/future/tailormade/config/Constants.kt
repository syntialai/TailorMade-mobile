package com.future.tailormade.config

object Constants {

  /**
   * Static data
   */
  const val MIN_DEBOUNCE_TIME = 300L
  const val MIN_PASSWORD_LENGTH = 8
  const val MIN_QUERY_SEARCH_LENGTH = 3
  const val INITIAL_PAGING_PAGE = 1
  const val INITIAL_PAGING_ITEM_PER_PAGE = 10
  const val TYPE_IMAGE_ALL = "image/*"

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
  const val FAILED_TO_GET_PROFILE_INFO = "$FAILED_TO get profile info"
  const val FAILED_TO_UPDATE_PROFILE = "$FAILED_TO update profile"
  const val COLOR_NAME_IS_EMPTY = "Color Name $IS_EMPTY"
  const val SIZE_NAME_IS_EMPTY = "Size Name $IS_EMPTY"
  const val CHEST_SIZE_IS_EMPTY = "Chest Size $IS_EMPTY"
  const val WAIST_SIZE_IS_EMPTY = "Waist Size $IS_EMPTY"
  const val HIPS_SIZE_IS_EMPTY = "Hips Size $IS_EMPTY"
  const val NECK_TO_WAIST_SIZE_IS_EMPTY = "Neck to Waist Size $IS_EMPTY"
  const val INSEAM_SIZE_IS_EMPTY = "Inseam Size $IS_EMPTY"

  const val MESSAGES_TYPE_TEXT = "TEXT"

  /*
   * Date Patterns
   */
  const val DD_MMMM_YYYY = "dd MMMM yyyy"
  const val HH_MM = "hh:mm"

  fun generateDeleteErrorMessage(
      category: String, id: String) = "$FAILED_TO delete $category with id $id"
}