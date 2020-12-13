package com.future.tailormade_chat.core.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RealtimeDbModule {

  @Provides
  @Singleton
  fun provideDatabaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference
}