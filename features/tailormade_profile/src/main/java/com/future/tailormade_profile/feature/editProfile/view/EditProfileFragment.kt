package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.debounceOnTextChanged
import com.future.tailormade.util.extension.isPhoneNumberValid
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade.util.extension.text
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade_profile.R
import com.future.tailormade_profile.databinding.FragmentEditProfileBinding
import com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

  companion object {
    fun newInstance() = EditProfileFragment()
  }

  private val viewModel: EditProfileViewModel by viewModels()

  private lateinit var binding: FragmentEditProfileBinding
  private lateinit var birthDatePicker: MaterialDatePicker<Long>

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.view.EditProfileFragment"

  override fun getScreenName(): String = "Edit Profile"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onNavigationIconClicked() {
    activity?.finish()
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    setupDatePicker()
    binding = FragmentEditProfileBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSubmitEditProfileForm.setOnClickListener {
        submitForm(editTextNameEditProfile.text(),
            editTextPhoneNumberEditProfile.text(), editTextLocationEditProfile.text())
      }

      editTextLocationEditProfile.debounceOnTextChanged(
          viewModel.viewModelScope, viewModel::updateLocations)

      textInputBirthDateEditProfile.setEndIconOnClickListener {
        showDatePicker()
      }
    }

    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()
    setupProfileDataObserver()
    setupLocationObserver()
  }

  private fun isFormValid(name: String, phoneNumber: String?) =
      name.isNotBlank() && viewModel.isBirthDateValid() && (phoneNumber?.isPhoneNumberValid().orTrue())

  private fun setFormErrorMessage() {
    with(binding) {
      textInputNameEditProfile.error = when {
        editTextNameEditProfile.text().isBlank() -> getString(R.string.name_is_empty)
        else -> null
      }

      textInputPhoneNumberEditProfile.error = when {
        editTextPhoneNumberEditProfile.text().isPhoneNumberValid().not() -> getString(
            R.string.phone_number_invalid)
        else -> null
      }

      textInputBirthDateEditProfile.error = when {
        editTextBirthDateEditProfile.text().isBlank() -> getString(R.string.birth_date_is_not_set)
        else -> null
      }
    }
  }

  private fun setupDatePicker() {
    birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(
        getString(R.string.birth_date_picker_title_label)).build()
    birthDatePicker.addOnPositiveButtonClickListener {
      binding.editTextBirthDateEditProfile.setText(it.toDateString(Constants.DD_MMMM_YYYY, true))
      viewModel.setBirthDate(it)
    }
  }

  private fun setupLocationObserver() {
    viewModel.listOfLocations.observe(viewLifecycleOwner, { items ->
      context?.let { context ->
        if (items.isNullOrEmpty().not()) {
          val adapter = ArrayAdapter(context, R.layout.layout_list_item_text, items)
          binding.editTextLocationEditProfile.setAdapter(adapter)
          adapter.notifyDataSetChanged()
        }
      }
    })
  }

  private fun setupProfileDataObserver() {
    viewModel.profileInfo.observe(viewLifecycleOwner, {
      with(binding) {
        editTextNameEditProfile.setText(it.name)
        editTextBirthDateEditProfile.setText(it.birthDate.toDateString(Constants.DD_MMMM_YYYY))
        editTextPhoneNumberEditProfile.setText(it.phoneNumber.orEmpty())
        editTextLocationEditProfile.setText(it.location?.address.orEmpty())
      }
    })
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager, getString(R.string.birth_date_picker_label))
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun submitForm(name: String, phoneNumber: String, location: String) {
    if (isFormValid(name, phoneNumber)) {
      viewModel.updateBasicInfo(name, phoneNumber, location)
    } else {
      setFormErrorMessage()
    }
  }
}