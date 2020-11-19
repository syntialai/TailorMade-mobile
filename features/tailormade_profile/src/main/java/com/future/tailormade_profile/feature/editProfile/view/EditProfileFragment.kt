package com.future.tailormade_profile.feature.editProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.debounceOnTextChanged
import com.future.tailormade.util.extension.isPhoneNumberValid
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

  private val viewModel: EditProfileViewModel by viewModels()

  private var birthDate: Long = 0L

  private lateinit var binding: FragmentEditProfileBinding
  private lateinit var birthDatePicker: MaterialDatePicker<Long>

  override fun getLogName(): String =
      "com.future.tailormade_profile.feature.editProfile.view.EditProfileFragment"

  override fun getScreenName(): String = "Edit Profile"

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    setupDatePicker()
    binding = FragmentEditProfileBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSubmitEditProfileForm.setOnClickListener {
        submitForm(editTextNameEditProfile.text.toString(),
            editTextPhoneNumberEditProfile.text.toString(),
            editTextBirthDateEditProfile.text.toString(),
            editTextLocationEditProfile.text.toString())
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

  private fun isFormValid(name: String, birthDate: String, phoneNumber: String?) =
      name.isNotBlank() && birthDate.isNotBlank() && (phoneNumber?.isPhoneNumberValid() ?: true)

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
        getString(R.string.birth_date_picker_label)).build()
    birthDatePicker.addOnPositiveButtonClickListener {
      birthDate = it
      binding.editTextBirthDateEditProfile.setText(
          it.toDateString(Constants.DD_MMMM_YYYY))
    }
  }

  private fun setupLocationObserver() {
    viewModel.listOfLocations.observe(viewLifecycleOwner, { items ->
      context?.let { context ->
        if (items.isNullOrEmpty().not()) {
          val adapter = ArrayAdapter(context, R.layout.layout_list_item_text,
              items)
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
        editTextBirthDateEditProfile.setText(
            it.birthDate.toDateString(Constants.DD_MMMM_YYYY))
        editTextPhoneNumberEditProfile.setText(it.phoneNumber.orEmpty())
        editTextLocationEditProfile.setText(it.location?.address.orEmpty())
      }
    })
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager,
        getString(R.string.birth_date_picker_label))
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun submitForm(name: String, phoneNumber: String, birthDate: String,
      location: String) {
    if (isFormValid(name, birthDate, phoneNumber)) {
      viewModel.updateBasicInfo(name, this.birthDate, phoneNumber, location)
    } else {
      setFormErrorMessage()
    }
  }

  companion object {

    @JvmStatic fun newInstance() = EditProfileFragment()
  }
}