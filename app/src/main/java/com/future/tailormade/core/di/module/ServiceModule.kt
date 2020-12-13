package com.future.tailormade.core.di.module

import com.future.tailormade.core.service.CartService
import com.future.tailormade.core.service.DashboardService
import com.future.tailormade.di.module.NetworkModule
import com.future.tailormade.di.scope.TailormadeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
class ServiceModule {

  @Provides
  fun provideDashboardService(@TailormadeApi retrofit: Retrofit): DashboardService {
    return retrofit.create(DashboardService::class.java)
  }

  @Provides
  fun provideCartService(@TailormadeApi retrofit: Retrofit): CartService {
    return retrofit.create(CartService::class.java)
  }
}