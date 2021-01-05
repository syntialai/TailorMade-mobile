package com.future.tailormade.tailor_app.core.di.module

import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade.tailor_app.core.service.DashboardService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {

  @Provides
  fun provideDashboardService(@TailormadeApi retrofit: Retrofit): DashboardService {
    return retrofit.create(DashboardService::class.java)
  }
}