package com.future.tailormade_profile.core.model.ui

import android.os.Parcelable
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileInfoUiModel(

    var id: String,

    var name: String,

    var image: String? = null,

    var phoneNumber: String? = null,

    var birthDate: Long = 0L,

    var address: String,

    var location: Location? = null,

    var occupation: Occupation? = null,

    var education: Education? = null): Parcelable
