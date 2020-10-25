package com.mta.blibli.tailormade_auth.core.model.response

data class TokenDetailResponse(

    var access: String? = null,

    var refresh: String? = null
)