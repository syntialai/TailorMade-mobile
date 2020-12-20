package com.future.tailormade_profile.core.model.ui

import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Location
import com.future.tailormade_profile.core.model.entity.Occupation

data class ProfileInfoUiModel(

    var id: String,

    var name: String,

    var image: String? = null,

    var phoneNumber: String? = null,

    var birthDate: Long = 0L,

    var address: String,

    var location: Location? = null,

    var occupation: Occupation? = null,

    var education: Education? = null)
