package com.future.tailormade_profile.core.service

import com.future.tailormade_profile.core.api.ProfileApiUrl
import com.future.tailormade_profile.core.model.response.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimService {

  @GET(ProfileApiUrl.NOMINATIM_API)
  fun searchLocation(@Query("query") query: String,
      @Query("format") format: String,
      @Query("addressdetails") addressDetails: Int,
      @Query("limit") limit: Int): List<LocationResponse>
}