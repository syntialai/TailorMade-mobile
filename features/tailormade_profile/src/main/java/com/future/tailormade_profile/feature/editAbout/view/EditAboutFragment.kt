package com.future.tailormade_profile.feature.editAbout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.debounceOnTextChanged
import com.future.tailormade.util.extension.text
import com.future.tailormade_profile.R
import com.future.tailormade_profile.core.model.entity.Education
import com.future.tailormade_profile.core.model.entity.Occupation
import com.future.tailormade_profile.databinding.FragmentEditAboutBinding
import com.future.tailormade_profile.feature.editAbout.viewmodel.EditAboutViewModel
import com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class EditAboutFragment : BaseFragment() {

  companion object {
    fun newInstance() = EditAboutFragment()
  }

  private val editAboutViewModel: EditAboutViewModel by viewModels()
  private val editProfileViewModel: EditProfileViewModel by viewModels()

  private lateinit var binding: FragmentEditAboutBinding

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editAbout.view.EditAboutFragment"

  override fun getScreenName(): String = "Edit About"

  override fun getViewModel(): BaseViewModel = editAboutViewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentEditAboutBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSubmitEditAboutForm.setOnClickListener {
        submitForm(editTextCompanyEditAbout.text(), editTextOccupationEditAbout.text(),
            editTextOccupationCityEditAbout.text(), editTextSchoolEditAbout.text(),
            editTextMajorEditAbout.text(), editTextEducationCityEditAbout.text())
      }

      editTextEducationCityEditAbout.debounceOnTextChanged(editProfileViewModel.viewModelScope,
          editProfileViewModel::updateLocations)

      editTextOccupationCityEditAbout.debounceOnTextChanged(editProfileViewModel.viewModelScope,
          editProfileViewModel::updateLocations)
    }

    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()
    setupAboutDataObserver()
    setupLocationObserver()
  }

  private fun setupAboutDataObserver() {
    editAboutViewModel.profileAbout.observe(viewLifecycleOwner, {
      with(binding) {
        editTextCompanyEditAbout.setText(it.occupation?.company.orEmpty())
        editTextOccupationEditAbout.setText(it.occupation?.job.orEmpty())
        editTextOccupationCityEditAbout.setText(it.occupation?.city.orEmpty())
        editTextSchoolEditAbout.setText(it.education?.school.orEmpty())
        editTextMajorEditAbout.setText(it.education?.major.orEmpty())
        editTextEducationCityEditAbout.setText(it.education?.city.orEmpty())
      }
    })
  }

  private fun setupLocationObserver() {
    editProfileViewModel.listOfLocations.observe(viewLifecycleOwner, { items ->
      context?.let { context ->
        if (items.isNullOrEmpty().not()) {
          val adapter = ArrayAdapter(context, R.layout.layout_list_item_text, items)
          binding.editTextEducationCityEditAbout.setAdapter(adapter)
          binding.editTextOccupationCityEditAbout.setAdapter(adapter)
          adapter.notifyDataSetChanged()
        }
      }
    })
  }

  @ExperimentalCoroutinesApi
  private fun submitForm(occupationCompany: String, occupationName: String, occupationCity: String,
      schoolName: String, schoolMajor: String, schoolCity: String) {
    val occupation = Occupation(occupationCompany, occupationCity, occupationName)
    val education = Education(schoolName, schoolMajor, schoolCity)
    editAboutViewModel.updateProfileAbout(occupation, education)
  }
}