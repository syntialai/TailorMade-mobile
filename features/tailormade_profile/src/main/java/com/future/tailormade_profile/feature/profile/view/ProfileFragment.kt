package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.image.ImageHelper
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentProfileBinding
import com.future.tailormade_profile.databinding.LayoutCardProfileWithEditBinding
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileFragment()
  }

  private val viewModel: ProfileViewModel by activityViewModels()

  private lateinit var fragmentProfileBinding: FragmentProfileBinding
  private lateinit var layoutCardProfileWithEditBinding: LayoutCardProfileWithEditBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileFragment"

  override fun getScreenName(): String = "Profile"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
    layoutCardProfileWithEditBinding = fragmentProfileBinding.layoutCardProfile
    layoutCardProfileWithEditBinding.buttonGoToEditProfile.setOnClickListener {
      goToEditProfile()
    }
    return fragmentProfileBinding.root
  }

  override fun onResume() {
    super.onResume()
    inflateFragment()
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchProfileInfo()
    viewModel.profileInfoUiModel.observe(viewLifecycleOwner, {
      it?.let { profileInfo ->
        val location = profileInfo.address
        setProfileData(profileInfo.name, location, profileInfo.image.orEmpty())
      }
    })
  }

  private fun goToEditProfile() {
    context?.let {
      Action.goToEditProfile(it, getString(R.string.type_profile))
    }
  }

  private fun inflateFragment() {
    with(fragmentProfileBinding.frameLayoutProfileContent) {
      val fragmentTransaction = parentFragmentManager.beginTransaction()
      fragmentTransaction.replace(this.id, ProfileAboutFragment.newInstance())
      fragmentTransaction.commit()
    }
  }

  private fun setProfileData(name: String, city: String, image: String) {
    with(layoutCardProfileWithEditBinding.layoutProfileInfo) {
      textViewProfileName.text = name
      textViewProfileCity.text = city

      context?.let {
        ImageLoader.loadImageUrlWithFitCenterAndPlaceholder(it, image,
            ImageHelper.getUserProfilePlaceholder(viewModel.getUserGender()), imageViewProfile)
      }
    }
  }
}