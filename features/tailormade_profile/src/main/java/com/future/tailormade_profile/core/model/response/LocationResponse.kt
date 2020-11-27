package com.future.tailormade_profile.core.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationResponse (

    @Expose
    val address: AddressResponse? = null,

    val boundingbox: List<String>? = null,

    @SerializedName("class")
    val className: String? = null,

    val display_name: String? = null,

    val importance: Double = 0.0,

    val lat: String = "",

    val lon: String = "",

    val osm_id: String? = null,

    val osm_type: String? = null,

    val place_id: String? = null,
)