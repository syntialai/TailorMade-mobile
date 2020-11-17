package com.future.tailormade_profile.core.di.module

import com.future.tailormade_profile.core.di.scope.ProfileScope
import com.future.tailormade_profile.core.repository.ProfileRepository
import com.future.tailormade_profile.core.repository.impl.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
@ProfileScope
abstract class ProfileBinderModule {

  @Binds
  abstract fun bindProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository
}