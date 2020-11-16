package com.future.tailormade_profile.core.di.component

import dagger.hilt.internal.GeneratedComponentManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileComponentHandler @Inject constructor(
    private val profileComponentFactory: ProfileComponent.Factory) :
    GeneratedComponentManager<ProfileComponent> {

  var profileComponent: ProfileComponent? = null
    private set

  fun login() {
    profileComponent = profileComponentFactory.create()
  }

  fun logout() {
    profileComponent = null
  }

  override fun generatedComponent(): ProfileComponent = profileComponent!!
}