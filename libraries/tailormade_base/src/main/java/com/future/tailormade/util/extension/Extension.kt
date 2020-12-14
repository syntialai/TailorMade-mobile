package com.future.tailormade.util.extension

import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.future.tailormade.base.view.ViewState
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.coroutine.CoroutineHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

/**
 * Flow extension functions
 */
fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.flowOnMain(): Flow<T> = this.flowOn(Dispatchers.Main)

@ExperimentalCoroutinesApi fun <T> Flow<T>.flowWithLoadingDialog(viewModel: BaseViewModel) = onStart {
	viewModel.viewState.value = ViewState.Loading(true)
}.onError {
	viewModel.viewState.value = ViewState.Loading(false)
}

@ExperimentalCoroutinesApi fun <T> Flow<T>.flowOnIOwithLoadingDialog(viewModel: BaseViewModel) = flowWithLoadingDialog(
    viewModel).flowOnIO()

@ExperimentalCoroutinesApi fun <T> Flow<T>.flowOnMainWithLoadingDialog(viewModel: BaseViewModel) = flowWithLoadingDialog(
    viewModel).flowOnMain()

fun <T> Flow<T>.onError(block: (error: Throwable) -> Unit): Flow<T> = catch { error ->
	block(error)
}

/**
 * View extension functions
 */
fun EditText.debounceOnTextChanged(scope: CoroutineScope, listener: (String) -> Unit) {
	doOnTextChanged { text, _, _, count ->
		val debounce = CoroutineHelper.debounce(scope = scope, destinationFunction = listener)
		if (count >= Constants.MIN_QUERY_SEARCH_LENGTH) {
			debounce.invoke(text.toString())
		}
	}
}

fun View.show() {
	visibility = View.VISIBLE
}

fun View.hide() {
	visibility = View.INVISIBLE
}

fun View.remove() {
	visibility = View.GONE
}

/**
 * Validate string extension functions
 */
fun String.isPhoneNumberValid(): Boolean = Patterns.PHONE.matcher(this).matches()

fun String.isEmailValid(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Date Converter
 */
fun Long.toDateString(pattern: String): String = SimpleDateFormat(pattern, Locale.ENGLISH).format(
    this)

fun Long.toDate(): Date = Date(this)

fun Int?.orZero() = this ?: 0
