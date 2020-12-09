package com.future.tailormade_design_detail.core.di.module

import com.future.tailormade_design_detail.core.repository.DesignDetailRepository
import com.future.tailormade_design_detail.core.repository.impl.DesignDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DesignDetailBinderModule {

  @Binds
  abstract fun bindDesignDetailRepository(
      designDetailRepositoryImpl: DesignDetailRepositoryImpl): DesignDetailRepository
}