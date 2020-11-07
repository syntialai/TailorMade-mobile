package com.future.tailormade_auth.core.di.module

import com.future.tailormade.di.module.NetworkModule
import com.future.tailormade_auth.core.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class) class AuthModule {

  @Provides @Singleton fun provideAuthService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
  }
}