package com.future.tailormade.core.di.module

import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.core.repository.impl.DashboardRepositoryImpl
import com.future.tailormade.core.repository.impl.OrderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun bindDashboardRepository(
      dashboardRepositoryImpl: DashboardRepositoryImpl): DashboardRepository

  @Binds
  abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository
}