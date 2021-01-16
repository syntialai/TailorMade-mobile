package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentProfileBinding
import com.future.tailormade_profile.databinding.LayoutCardProfileWithEditBinding
import com.future.tailormade_profile.feature.editProfile.adapter.ProfilePagerAdapter
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import com.future.tailormade_router.actions.Action
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileFragment()
  }

  private val viewModel: ProfileViewModel by viewModels()

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
      context?.let {
        Action.goToEditProfile(it, Constants.TYPE_PROFILE)
      }
    }
    layoutCardProfileWithEditBinding.layoutProfileInfo.buttonChatTailor.setOnClickListener {
      // TODO: Route to chat with tailor
    }
    inflateFragment()
    return fragmentProfileBinding.root
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchProfileInfo()
    viewModel.profileInfoUiModel.observe(viewLifecycleOwner, {
      it?.let { profileInfo ->
        val location = profileInfo.address
        setButtonVisibility(profileInfo.id)
        setProfileData(profileInfo.name, location, profileInfo.image.orEmpty())
        setupFragment(profileInfo.id)
      }
    })
  }

  private fun setupFragment(userId: String) {
    viewModel.isUserProfile(userId).let {
      if (it) {
        setupViewPager()
        setupTabLayout()
      } else {
        inflateFragment()
      }
    }
  }

  private fun inflateFragment() {
    with(fragmentProfileBinding.frameLayoutProfileContent) {
      show()
      val fragmentTransaction = parentFragmentManager.beginTransaction()
      fragmentTransaction.replace(this.id, ProfileAboutFragment.newInstance())
      fragmentTransaction.commit()
    }
  }

  private fun setupTabLayout() {
    with(fragmentProfileBinding) {
      TabLayoutMediator(tabLayoutProfile, viewPagerProfile) { tab, position ->
        tab.text = when (position) {
          ProfilePagerAdapter.DESIGN_FRAGMENT_INDEX -> getString(R.string.design_label)
          ProfilePagerAdapter.ABOUT_FRAGMENT_INDEX -> getString(R.string.about_label)
          else -> ""
        }
        viewPagerProfile.setCurrentItem(tab.position, true)
      }.attach()
    }
  }

  private fun setupViewPager() {
    fragmentProfileBinding.viewPagerProfile.adapter = ProfilePagerAdapter(parentFragmentManager,
        lifecycle)
  }

  private fun setButtonVisibility(userId: String) {
    with(layoutCardProfileWithEditBinding) {
      if (viewModel.isUserProfile(userId)) {
        buttonGoToEditProfile.show()
      } else {
        layoutProfileInfo.buttonChatTailor.show()
      }
    }
  }

  private fun setProfileData(name: String, city: String, image: String) {
    with(layoutCardProfileWithEditBinding.layoutProfileInfo) {
      textViewProfileName.text = name
      textViewProfileCity.text = city

      context?.let {
        if (image.isNotBlank()) {
          ImageLoader.loadImageUrl(it, image, imageViewProfile)
        }
      }
    }
  }
}