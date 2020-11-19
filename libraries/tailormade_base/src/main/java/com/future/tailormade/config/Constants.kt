package com.future.tailormade.config

object Constants {

    /**
     * Static data
     */
    const val MIN_PASSWORD_LENGTH = 8

    /**
     * Error messages
     */
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

    /*
     * Date Patterns
     */
    const val DD_MMMM_YYYY = "dd MMMM yyyy"

    /*
     * Other
     */
    const val BIRTH_DATE_PICKER = "Birth Date Picker"
    const val BIRTH_DATE_PICKER_TITLE = "Choose Date"
}