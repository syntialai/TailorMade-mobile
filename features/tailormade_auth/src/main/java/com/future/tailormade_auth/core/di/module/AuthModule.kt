package com.future.tailormade_auth.core.di.module

import com.future.tailormade.di.module.NetworkModule
import com.future.tailormade.di.scope.FirebaseApi
import com.future.tailormade.di.scope.TailormadeLoginApi
import com.future.tailormade_auth.core.service.AuthService
import com.future.tailormade_auth.core.service.FirebaseAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
class AuthModule {

  @Provides
  fun provideAuthService(@TailormadeLoginApi retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
  }

  @Provides
  fun provideFirebaseAuthService(@FirebaseApi retrofit: Retrofit): FirebaseAuthService {
    return retrofit.create(FirebaseAuthService::class.java)
  }
}