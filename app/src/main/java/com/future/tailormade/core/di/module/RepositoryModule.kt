package com.future.tailormade.core.di.module

import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.core.repository.DashboardRepository
import com.future.tailormade.core.repository.impl.CartRepositoryImpl
import com.future.tailormade.core.repository.impl.CheckoutRepositoryImpl
import com.future.tailormade.core.repository.impl.DashboardRepositoryImpl
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
  abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

  @Binds
  abstract fun bindCheckoutRepository(
      checkoutRepositoryImpl: CheckoutRepositoryImpl): CheckoutRepository
}