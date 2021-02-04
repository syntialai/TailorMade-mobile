package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.show
import com.future.tailormade_profile.R
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.databinding.FragmentProfileAboutBinding
import com.future.tailormade_profile.feature.profile.viewModel.ProfileViewModel
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class ProfileAboutFragment : BaseFragment() {

  companion object {
    fun newInstance() = ProfileAboutFragment()
  }

  private val viewModel: ProfileViewModel by activityViewModels()

  private lateinit var binding: FragmentProfileAboutBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileAboutFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileAboutBinding.inflate(inflater, container, false)
    binding.textViewEditAbout.setOnClickListener {
      goToEditProfileAbout()
    }
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.profileInfoUiModel.observe(viewLifecycleOwner, {
      it?.let { profile ->
        if (viewModel.isUser()) {
          binding.textViewEditAbout.show()
        }

        profile.location?.address?.let { address ->
          setAddressData(address)
        }
        profile.occupation?.let { occupation ->
          if (occupation.company.isNullOrBlank().not() || occupation.job.isNullOrBlank().not()) {
            setOccupationData(occupation)
          }
        }
        profile.education?.let { education ->
          if (education.school.isNullOrBlank().not() || education.major.isNullOrBlank().not()) {
            setEducationData(education)
          }
        }
      }
    })
  }

  private fun goToEditProfileAbout() {
    context?.let {
      Action.goToEditProfile(it, getString(R.string.type_about))
    }
  }

  private fun setAddressData(address: String) {
    with(binding) {
      imageViewAboutAddress.show()
      textViewAboutAddressValue.show()
      textViewAboutAddressValue.text = getString(R.string.about_address_value, address)
    }
  }

  private fun setEducationData(education: Education) {
    with(binding) {
      imageViewAboutSchool.show()
      textViewAboutSchoolValue.show()

      val schoolValue = getStringValue(education.school, R.string.about_school_value)
      val majorValue = getStringValue(education.major, R.string.about_school_major_value)
      textViewAboutSchoolValue.text = getString(R.string.studied_label) + schoolValue + majorValue
    }
  }

  private fun setOccupationData(occupation: Occupation) {
    with(binding) {
      imageViewAboutOccupation.show()
      textViewAboutOccupationValue.show()

      val occupationValue = getStringValue(occupation.job, R.string.about_occupation_value)
      val companyValue = getStringValue(occupation.company, R.string.about_occupation_company_value)
      textViewAboutOccupationValue.text = getString(R.string.works_label) + occupationValue + companyValue
    }
  }

  private fun getStringValue(text: String?, templateId: Int) = if (text.isNullOrBlank()) {
    ""
  } else {
    " ${getString(templateId, text)}"
  }
}