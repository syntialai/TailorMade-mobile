package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.ActivityEditProfileBinding
import com.future.tailormade_profile.feature.editAbout.view.EditAboutFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

  companion object {
    private const val PARAM_EDIT_PROFILE_TYPE = "PARAM_EDIT_PROFILE_TYPE"
  }

  private lateinit var binding: ActivityEditProfileBinding

  private lateinit var navHostFragment: NavHostFragment
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEditProfileBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarProfile
    setContentView(binding.root)
    setSupportActionBar(toolbar)
    setupOnNavigationIconClicked {
      finish()
    }
    setupToolbar(getScreenName())
    setupNavController()
    intent.getStringExtra(PARAM_EDIT_PROFILE_TYPE)?.let {
      if (it == Constants.TYPE_PROFILE) {
        navController.navigate(EditProfileFragmentDirections.actionGlobalEditProfileFragment())
      } else if (it == Constants.TYPE_ABOUT) {
        navController.popBackStack()
        navController.navigate(EditAboutFragmentDirections.actionGlobalEditAboutFragment())
      }
    }
  }

  private fun setupNavController() {
    navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_profile_fragment) as NavHostFragment
    navController = navHostFragment.navController
  }
}