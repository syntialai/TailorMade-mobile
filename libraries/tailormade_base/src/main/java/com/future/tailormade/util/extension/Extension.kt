package com.future.tailormade.util.extension

import android.util.Patterns
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.*

/**
 * Flow extension functions
 */
fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.flowOnMain(): Flow<T> = this.flowOn(Dispatchers.Main)

fun <T> Flow<T>.onError(block: (error: Throwable) -> Unit): Flow<T> =
    catch { error -> block(error) }

fun View.remove() {
    this.visibility = View.GONE
}

/**
 * Validate string extension functions
 */
fun String.isPhoneNumberValid(): Boolean = Patterns.PHONE.matcher(this).matches()

fun String.isEmailValid(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Date Converter
 */
fun Long.toDateString(pattern: String): String = SimpleDateFormat(pattern, Locale.ENGLISH).format(this)

fun Long.toDate(): Date = Date(this)

/**
 * Null handling functions
 */
fun <T> List<T>?.orEmptyList(): List<T> = this ?: listOf()

fun Int?.orZero(): Int = this ?: 0

fun Double?.orZero(): Double = this ?: 0.0

fun Long?.orZero(): Long = this ?: 0L

fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean?.orFalse(): Boolean = this ?: false