package com.future.tailormade_profile.feature.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

@AndroidEntryPoint
class ProfileAboutFragment : BaseFragment(), View.OnClickListener {

  companion object {
    fun newInstance() = ProfileAboutFragment()
  }

  private val viewModel: ProfileViewModel by viewModels()

  private lateinit var binding: FragmentProfileAboutBinding

  override fun getLogName() = "com.future.tailormade_profile.feature.profile.view.ProfileAboutFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentProfileAboutBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onClick(view: View?) {
    when(view) {
      binding.textViewEditAbout -> goToEditProfileAbout()
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.profileInfoUiModel.observe(viewLifecycleOwner, {
      it?.location?.address?.let { address ->
        setAddressData(address)
      }

      it?.occupation?.let { occupation ->
        if (occupation.company.isNullOrBlank().not() || occupation.job.isNullOrBlank().not()) {
          setOccupationData(occupation)
        }
      }

      it?.education?.let { education ->
        if (education.school.isNullOrBlank().not() || education.major.isNullOrBlank().not()) {
          setEducationData(education)
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
    binding.textViewAboutAddressValue.text = getString(R.string.about_address_value, address)
  }

  private fun setEducationData(education: Education) {
    binding.imageViewAboutSchool.show()
    binding.textViewAboutSchoolValue.show()

    (getString(R.string.studied_label) + education.school?.let {
      getString(R.string.about_school_value, it)
    }.orEmpty() + education.major?.let {
      getString(R.string.about_school_major_value, it)
    }.orEmpty()).also {
      binding.textViewAboutSchoolValue.text = it
    }
  }

  private fun setOccupationData(occupation: Occupation) {
    binding.imageViewAboutOccupation.show()
    binding.textViewAboutOccupationValue.show()

    (getString(R.string.works_label) + occupation.job?.let {
      getString(R.string.about_occupation_value, it)
    }.orEmpty() + occupation.company?.let {
      getString(R.string.about_occupation_company_value, it)
    }.orEmpty()).also {
      binding.textViewAboutOccupationValue.text = it
    }
  }
}