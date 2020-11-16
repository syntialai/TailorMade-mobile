package com.future.tailormade_profile.core.di.component

import dagger.hilt.DefineComponent
import dagger.hilt.android.components.ApplicationComponent

@DefineComponent(parent = ApplicationComponent::class)
interface ProfileComponent {

  @DefineComponent.Builder
  interface Factory {
    fun create(): ProfileComponent
  }
}