package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_profile.databinding.ActivityEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

  private lateinit var binding: ActivityEditProfileBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditProfileBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarProfile
    setContentView(binding.root)
  }
}