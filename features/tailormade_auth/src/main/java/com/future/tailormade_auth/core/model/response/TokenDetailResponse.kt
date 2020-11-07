package com.future.tailormade_auth.core.model.response

data class TokenDetailResponse(

    var access: String? = null,

    var refresh: String? = null
)