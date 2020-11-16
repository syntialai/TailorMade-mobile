package com.future.tailormade_profile.core.di.module

import com.future.tailormade_profile.core.di.component.ProfileComponent
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.repository.impl.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@Module
@InstallIn(ProfileComponent::class)
interface ProfileBinderModule {

  @Binds
  abstract fun bindProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}