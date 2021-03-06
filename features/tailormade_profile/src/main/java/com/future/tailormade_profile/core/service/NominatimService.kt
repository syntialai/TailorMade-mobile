package com.future.tailormade_profile.core.service

import com.future.tailormade_profile.core.api.ProfileApiUrl
import com.future.tailormade_profile.core.model.response.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NominatimService {

  @GET(ProfileApiUrl.NOMINATIM_SEARCH_BY_QUERY_PATH)
  suspend fun searchLocation(
      @Path("query") query: String, @Query("format") format: String,
      @Query("addressdetails") addressDetails: String,
      @Query("limit") limit: String, @Query("svg") svg: String): ArrayList<LocationResponse>
}