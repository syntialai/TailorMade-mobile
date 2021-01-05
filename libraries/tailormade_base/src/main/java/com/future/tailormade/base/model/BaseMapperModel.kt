package com.future.tailormade.base.model

data class BaseMapperModel<T, U>(

    val response: T,

    val uiModel: U
)