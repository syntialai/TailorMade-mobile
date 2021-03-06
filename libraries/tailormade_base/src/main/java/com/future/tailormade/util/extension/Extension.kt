package com.future.tailormade.util.extension

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.widget.doOnTextChanged
import com.future.tailormade.config.Constants
import com.future.tailormade.util.coroutine.CoroutineHelper
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Flow extension functions
 */
fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.onError(block: (error: Throwable) -> Unit): Flow<T> = catch { error ->
  block(error)
}

/**
 * View extension functions
 */
fun TextInputLayout.reset(editText: EditText? = null) {
  error = null
  editText?.reset()
}

fun EditText.reset() {
  error = null
  setText("")
}

fun EditText.debounceOnTextChanged(scope: CoroutineScope, listener: (String) -> Unit) {
  doOnTextChanged { text, _, _, count ->
    val debounce = CoroutineHelper.debounce(scope = scope, destinationFunction = listener)
    if (count >= Constants.MIN_QUERY_SEARCH_LENGTH) {
      debounce.invoke(text.toString())
    }
  }
}

fun EditText.validateInput(textInputLayout: TextInputLayout, errorMessage: String?,
    vararg validators: (String) -> Pair<Boolean, String>) {
  doOnTextChanged { text, _, _, _ ->
    if (text.isNullOrBlank()) {
      errorMessage?.let {
        textInputLayout.error = it
      }
      return@doOnTextChanged
    }
    validators.forEach { validator ->
      val validateResult = validator.invoke(text.toString())
      if (validateResult.first.not()) {
        textInputLayout.error = validateResult.second
        return@doOnTextChanged
      }
    }
    textInputLayout.error = null
  }
}

fun EditText.text() = this.text.toString()

fun TextView.text() = this.text.toString()

fun TextView.strikeThrough() {
  this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun View.hide() {
  visibility = View.INVISIBLE
}

fun View.remove() {
  visibility = View.GONE
}

fun View.show() {
  visibility = View.VISIBLE
}

fun ViewGroup.hide() {
  this.visibility = View.INVISIBLE
}

fun ViewGroup.remove() {
  this.visibility = View.GONE
}

fun ViewGroup.show() {
  this.visibility = View.VISIBLE
}

fun View.setVisibility(value: Boolean) {
  if (value) {
    this.show()
  } else {
    this.remove()
  }
}

@ColorRes
fun Context.getColorResFromAttrs(@AttrRes attr: Int): Int {
  val attrs = intArrayOf(attr)
  val typedArray: TypedArray = obtainStyledAttributes(attrs)
  val colorRes: Int = typedArray.getResourceId(0, android.R.color.black)
  typedArray.recycle()
  return colorRes
}

/**
 * Validate string extension functions
 */
fun String.isPhoneNumberValid(): Boolean = Patterns.PHONE.matcher(
    this).matches() && this.length >= Constants.MIN_PHONE_NUMBER_LENGTH

fun String.isEmailValid(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Date Time Converter
 */
fun Long.toDateString(pattern: String, isTimestamp: Boolean = false): String {
  val time = if (isTimestamp) {
    1
  } else {
    1000
  }
  return SimpleDateFormat(pattern, Locale.ENGLISH).format(this * time)
}

/**
 * Money Converter
 */
fun Double.toIndonesiaCurrencyFormat(): String = NumberFormat.getCurrencyInstance().apply {
  maximumFractionDigits = 0
  currency = Currency.getInstance(Locale("in", "ID"))
}.format(this)

/**
 * Null handling functions
 */
fun <T> List<T>?.orEmptyList(): List<T> = this ?: listOf()

fun <T> ArrayList<T>?.orEmptyList(): ArrayList<T> = this ?: arrayListOf()

fun <T> MutableList<T>?.orEmptyMutableList(): MutableList<T> = this ?: mutableListOf()

fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0f

fun Double?.orZero(): Double = this ?: 0.0

fun Long?.orZero(): Long = this ?: 0L

fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean?.orFalse(): Boolean = this ?: false