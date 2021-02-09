package com.future.tailormade.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ApplicationComponent::class)
class DispatchersModule {

  @Provides
  @Singleton
  fun provideIoDispatchers(): CoroutineDispatcher = Dispatchers.IO
}