package com.future.tailormade.tailor_app.core.di.module

import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade.tailor_app.core.service.DashboardService
import com.future.tailormade.tailor_app.core.service.OrderService
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

  @Provides
  fun provideOrderService(@TailormadeApi retrofit: Retrofit): OrderService {
    return retrofit.create(OrderService::class.java)
  }
}