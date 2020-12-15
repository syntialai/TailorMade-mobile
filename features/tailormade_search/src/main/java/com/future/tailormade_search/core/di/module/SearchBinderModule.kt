package com.future.tailormade_search.core.di.module

import com.future.tailormade_search.core.di.scope.SearchScope
import com.future.tailormade_search.core.repository.SearchRepository
import com.future.tailormade_search.core.repository.impl.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
@SearchScope
abstract class SearchBinderModule {

  @Binds
  abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}