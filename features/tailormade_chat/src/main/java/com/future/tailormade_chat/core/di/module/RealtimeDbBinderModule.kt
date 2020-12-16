package com.future.tailormade_chat.core.di.module

import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.future.tailormade_chat.core.repository.impl.RealtimeDbRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RealtimeDbBinderModule {

  @Binds
  abstract fun bindRealtimeDbRepository(
      realtimeDbRepositoryImpl: RealtimeDbRepositoryImpl): RealtimeDbRepository
}