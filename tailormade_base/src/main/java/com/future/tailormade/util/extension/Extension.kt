package com.future.tailormade.util.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

fun <T> Flow<T>.flowOnIO(): Flow<T> = this.flowOn(Dispatchers.IO)

fun <T> Flow<T>.flowOnMain(): Flow<T> = this.flowOn(Dispatchers.Main)