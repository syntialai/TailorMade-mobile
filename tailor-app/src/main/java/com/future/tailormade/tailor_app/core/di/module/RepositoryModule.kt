package com.future.tailormade.tailor_app.core.di.module

import com.future.tailormade.tailor_app.core.repository.DashboardRepository
import com.future.tailormade.tailor_app.core.repository.OrderRepository
import com.future.tailormade.tailor_app.core.repository.impl.DashboardRepositoryImpl
import com.future.tailormade.tailor_app.core.repository.impl.OrderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun bindDashboardRepository(
      dashboardRepositoryImpl: DashboardRepositoryImpl): DashboardRepository

  @Binds
  abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository
}