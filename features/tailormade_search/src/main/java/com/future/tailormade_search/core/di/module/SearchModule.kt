package com.future.tailormade_search.core.di.module

import com.future.tailormade.di.scope.TailormadeApi
import com.future.tailormade_search.core.di.scope.SearchScope
import com.future.tailormade_search.core.service.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
@SearchScope
class SearchModule {

  @Provides
  fun provideSearchService(@TailormadeApi retrofit: Retrofit): SearchService {
    return retrofit.create(SearchService::class.java)
  }
}