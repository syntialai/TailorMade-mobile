package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_profile.databinding.FragmentProfileBinding
import com.future.tailormade_profile.databinding.LayoutCardProfileWithEditBinding
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileFragment()
  }

  private val viewModel: ProfileViewModel by viewModels()

  private lateinit var fragmentProfileBinding: FragmentProfileBinding
  private lateinit var layoutCardProfileWithEditBinding: LayoutCardProfileWithEditBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
    layoutCardProfileWithEditBinding = fragmentProfileBinding.layoutCardProfile
    layoutCardProfileWithEditBinding.buttonGoToEditProfile.setOnClickListener {
      // TODO: Route to edit profile info
    }
    layoutCardProfileWithEditBinding.layoutProfileInfo.buttonChatTailor.setOnClickListener {
      // TODO: Route to chat with tailor
    }
    return fragmentProfileBinding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.profileInfoUiModel.observe(viewLifecycleOwner, {
      val location = it.address
      setButtonVisibility(it.id)
      setProfileData(it.name, location, it.image.orEmpty())
    })
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
          ImageLoader.loadImageUrl(requireContext(), image, imageViewProfile)
        }
      }
    }
  }
}