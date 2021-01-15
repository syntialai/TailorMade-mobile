package com.future.tailormade_search.core.di.module

import com.future.tailormade_search.core.repository.SearchRepository
import com.future.tailormade_search.core.repository.impl.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SearchBinderModule {

  @Binds
  abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}