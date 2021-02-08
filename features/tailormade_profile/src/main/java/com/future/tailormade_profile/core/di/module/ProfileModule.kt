package com.future.tailormade_profile.core.di.module

import com.future.tailormade.di.scope.OtherApi
import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade_profile.core.api.ProfileApiUrl
import com.future.tailormade_profile.core.service.NominatimService
import com.future.tailormade_profile.core.service.ProfileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
class ProfileModule {

  @Provides
  fun providesProfileService(@TailormadeApi retrofit: Retrofit): ProfileService {
    return retrofit.create(ProfileService::class.java)
  }

  @Provides
  @OtherApi
  fun providesNominatimRetrofit(@OtherApi builder: Retrofit.Builder): Retrofit {
    return builder.baseUrl(ProfileApiUrl.BASE_NOMINATIM_API_PATH).build()
  }

  @Provides
  fun providesNominatimService(@OtherApi retrofit: Retrofit): NominatimService {
    return retrofit.create(NominatimService::class.java)
  }
}