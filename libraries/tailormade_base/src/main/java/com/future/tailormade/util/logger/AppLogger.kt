package com.future.tailormade.util.logger

import timber.log.Timber

class AppLogger(private val logName: String) {

    fun logLifecycleOnCreate() {
        logInfo("Lifecycle OnCreate")
    }

    fun logLifecycleOnStart() {
        logInfo("Lifecycle OnStart")
    }

    fun logLifecycleOnResume() {
        logInfo("Lifecycle OnResume")
    }

    fun logLifecycleOnPause() {
        logInfo("Lifecycle OnPause")
    }

    fun logLifecycleOnStop() {
        logInfo("Lifecycle OnStop")
    }

    fun logLifecycleOnDestroy() {
        logInfo("Lifecycle OnDestroy")
    }

    fun logLifecycleOnAttach() {
        logInfo("Lifecycle OnAttach")
    }

    fun logLifecycleOnDetach() {
        logInfo("Lifecycle OnDetach")
    }

    fun logLifecycleOnCreateView() {
        logInfo("Lifecycle OnCreateView")
    }

    fun logLifecycleOnViewCreated() {
        logInfo("Lifecycle OnViewCreated")
    }

    fun logLifecycleOnDestroyView() {
        logInfo("Lifecycle OnDestroyView")
    }

    fun logLifecycleOnActivityCreated() {
        logInfo("Lifecycle OnActivityCreated")
    }

    fun logOnClick(viewName: String) {
        logInfo("$viewName OnClick")
    }

    fun logOnTextChanged(viewName: String) {
        logInfo("$viewName onTextChanged")
    }

    fun logOnRefresh(viewName: String) {
        logInfo("$viewName onRefresh")
    }

    fun logOnSwipe(viewName: String) {
        logInfo("$viewName onSwipe")
    }

    fun logOnTabSelected(tabName: String) {
        logInfo("Tab $tabName is selected")
    }

    fun logOnMethod(methodName: String) {
        logInfo("$methodName Triggered")
    }

    fun logOnEvent(eventDescription: String) {
        logInfo(eventDescription)
    }

    fun logOnError(message: String, throwable: Throwable) {
        logError(message)
        Timber.tag(logName).e(throwable)
    }

    fun logApiOnCall(url: String) {
        logInfo("API Call $url")
    }

    fun logApiOnSuccess(url: String, body: String) {
        logInfo("Success API call $url : $body")
    }

    fun logApiOnError(url: String, errorCode: String?, errorMessage: String?) {
        logError("Error API call $url (${errorCode.orEmpty()}) ${errorMessage.orEmpty()}")
    }

    private fun logInfo(message: String) {
        Timber.tag(logName).i(message)
    }

    private fun logError(errorMessage: String) {
        Timber.tag(logName).e(errorMessage)
    }
}