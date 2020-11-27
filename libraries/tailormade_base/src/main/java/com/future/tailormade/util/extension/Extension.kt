package com.future.tailormade.util.extension

import android.os.Build
import android.util.Patterns
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Flow extension functions
 */
fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.flowOnMain(): Flow<T> = this.flowOn(Dispatchers.Main)

fun <T> Flow<T>.onError(block: (error: Throwable) -> Unit): Flow<T> = catch { error ->
  block(error)
}

fun View.show() {
  this.visibility = View.VISIBLE
}

fun View.remove() {
  this.visibility = View.GONE
}

fun View.setVisibility(value: Boolean) {
  if (value) {
    this.show()
  } else {
    this.remove()
  }
}

/**
 * Validate string extension functions
 */
fun String.isPhoneNumberValid(): Boolean = Patterns.PHONE.matcher(this).matches()

fun String.isEmailValid(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Date Converter
 */
fun Long.toDateString(pattern: String): String = SimpleDateFormat(pattern,
    Locale.ENGLISH).format(this)

fun Long.toDate(): Date = Date(this)

fun Timestamp.toTimeString(pattern: String): String = SimpleDateFormat(pattern,
    Locale.ENGLISH).format(this)

/**
 * Collection converter
 */
@RequiresApi(Build.VERSION_CODES.N)
fun <T> MutableMap<String, T>.getFirstElement() = this.entries.stream().findFirst().get()
