package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isPhoneNumberValid
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade_profile.databinding.EditProfileFragmentBinding
import com.future.tailormade_profile.feature.editProfile.viewModel.EditProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

  private val viewModel: EditProfileViewModel by viewModels()

  private lateinit var binding: EditProfileFragmentBinding

  private lateinit var birthDatePicker: MaterialDatePicker<Long>

  override fun getScreenName(): String = "com.future.tailormade_profile.feature.editProfile.view.EditProfileFragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    setupDatePicker()

    binding = EditProfileFragmentBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSubmitEditProfileForm.setOnClickListener {
        submitForm(editTextNameEditProfile.text.toString(),
            editTextPhoneNumberEditProfile.text.toString(),
            editTextBirthDateEditProfile.text.toString(),
            editTextLocationEditProfile.text.toString())
      }

      textInputBirthDateEditProfile.setEndIconOnClickListener {
        showDatePicker()
      }

      textInputLocationEditProfile.setEndIconOnClickListener {
        showLocationPicker()
      }
    }

    return binding.root
  }

  private fun isFormValid(name: String, birthDate: String, phoneNumber: String?) =
    name.isNotBlank() && birthDate.isNotBlank() && (phoneNumber?.isPhoneNumberValid()
                                                                              ?: true)

  private fun setFormErrorMessage() {
    with(binding) {
      textInputNameEditProfile.error = when {
        editTextNameEditProfile.text.toString().isBlank() -> Constants.NAME_IS_EMPTY
        else -> null
      }

      textInputPhoneNumberEditProfile.error = when {
        editTextPhoneNumberEditProfile.text.toString().isPhoneNumberValid().not() -> Constants.PHONE_NUMBER_IS_NOT_VALID
        else -> null
      }

      textInputBirthDateEditProfile.error = when {
        editTextBirthDateEditProfile.text.toString().isBlank() -> Constants.BIRTH_DATE_IS_NOT_SET
        else -> null
      }
    }
  }

  private fun setupDatePicker() {
    birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(
        Constants.BIRTH_DATE_PICKER_TITLE).build()
    birthDatePicker.addOnPositiveButtonClickListener {
      binding.editTextBirthDateEditProfile.setText(
          it.toDateString(Constants.DD_MMMM_YYYY))
    }
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager, Constants.BIRTH_DATE_PICKER)
  }

  private fun showLocationPicker() {
    // TODO: Implement location picker
  }

  private fun submitForm(name: String, phoneNumber: String, birthDate: String,
      location: String) {
    if (isFormValid(name, birthDate, phoneNumber)) {
      viewModel.updateBasicInfo(name, phoneNumber, birthDate, location)
    } else {
      setFormErrorMessage()
    }
  }

  companion object {

    @JvmStatic fun newInstance() = EditProfileFragment()
  }
}