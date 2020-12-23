package com.future.tailormade_design_detail.core.di.module

import com.future.tailormade.di.module.NetworkModule
import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade_design_detail.core.service.DesignDetailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
class DesignDetailModule {

  @Provides
  fun provideDesignDetailService(@TailormadeApi retrofit: Retrofit): DesignDetailService {
    return retrofit.create(DesignDetailService::class.java)
  }
}