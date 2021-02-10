package com.future.tailormade_profile.core.model.response

import android.os.Parcelable
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileInfoResponse(

    val id: String,

    val name: String,

    val image: String? = null,

    val phoneNumber: String? = null,

    val birthDate: Long = 0L,

    val location: Location? = null,

    val occupation: Occupation? = null,

    val education: Education? = null) : Parcelable