package com.future.tailormade_auth.core.di.module

import android.content.Context
import com.future.tailormade.di.module.NetworkModule
import com.future.tailormade.di.scope.FirebaseApi
import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_auth.core.service.AuthService
import com.future.tailormade_auth.core.service.FirebaseAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
class AuthModule {

  @Provides
  fun provideAuthService(@TailormadeApi retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
  }

  @Provides
  fun provideFirebaseAuthService(@FirebaseApi retrofit: Retrofit): FirebaseAuthService {
    return retrofit.create(FirebaseAuthService::class.java)
  }

  @Provides
  @Singleton
  fun provideAuthSharedPref(@ApplicationContext context: Context): AuthSharedPrefRepository {
    return AuthSharedPrefRepository.newInstance(context)
  }
}