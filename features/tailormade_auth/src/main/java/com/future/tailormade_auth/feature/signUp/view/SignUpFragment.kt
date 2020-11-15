package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade.util.view.ToastHelper
import com.future.tailormade_auth.databinding.FragmentSignUpBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

  private val viewModel: SignUpViewModel by viewModels()

  private lateinit var binding: FragmentSignUpBinding

  private lateinit var birthDatePicker: MaterialDatePicker<Long>

  override fun getScreenName(): String =
    "com.future.tailormade_auth.feature.signUp.view.SignUpFragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    setupDatePicker()

    binding = FragmentSignUpBinding.inflate(inflater, container, false)

    with(binding) {
      textInputBirthDateSignUp.setEndIconOnClickListener {
        showDatePicker()
      }

      buttonSubmitForm.setOnClickListener {
        submitForm(
          editTextNameSignUp.text.toString(),
          editTextEmailSignUp.text.toString(),
          editTextBirthDateSignUp.text.toString(),
          editTextPasswordSignUp.text.toString(),
          editTextConfirmPasswordSignUp.text.toString(),
        )
      }

      buttonGoToSignIn.setOnClickListener {
        findNavController().navigate(
          SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        )
      }
    }

    return binding.root
  }

  private fun isFormValid(
    name: String, email: String, birthDate: String,
    password: String, confirmPassword: String
  ): Boolean =
    name.isNotBlank() && email.isNotBlank() && email.isEmailValid() && birthDate.isNotBlank() && password.isNotBlank() && password.length >= 8 && confirmPassword.isNotBlank() && confirmPassword == password

  private fun setFormErrorMessage() {
    with(binding) {
      textInputNameSignUp.error = when {
        editTextNameSignUp.text.toString().isBlank() -> Constants.NAME_IS_EMPTY
        else -> null
      }

      textInputEmailSignUp.error = when {
        editTextEmailSignUp.text.toString().isBlank() -> Constants.EMAIL_IS_EMPTY
        editTextEmailSignUp.text.toString().isEmailValid().not() -> Constants.EMAIL_IS_NOT_VALID
        else -> null
      }

      textInputBirthDateSignUp.error = when {
        editTextBirthDateSignUp.text.toString().isBlank() -> Constants.BIRTH_DATE_IS_NOT_SET
        else -> null
      }

      textInputPasswordSignUp.error = when {
        editTextPasswordSignUp.text.toString().isBlank() -> Constants.PASSWORD_IS_EMPTY
        editTextPasswordSignUp.text.toString().length <= 8 -> Constants.PASSWORD_IS_NOT_VALID
        else -> null
      }

      textInputConfirmPasswordSignUp.error = when {
        editTextConfirmPasswordSignUp.text.toString()
          .isBlank() -> Constants.CONFIRM_PASSWORD_IS_EMPTY
        editTextConfirmPasswordSignUp.text.toString() != editTextPasswordSignUp.text.toString() -> Constants.CONFIRM_PASSWORD_MUST_BE_SAME_WITH_PASSWORD
        else -> null
      }
    }
  }

  private fun setupDatePicker() {
    birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(
        "Choose Date").build()
    birthDatePicker.addOnPositiveButtonClickListener {
      binding.editTextBirthDateSignUp.setText(it.toDateString(Constants.DD_MMMM_YYYY))
    }
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager, BIRTH_DATE_PICKER)
  }

  private fun submitForm(
    name: String, email: String, birthDate: String,
    password: String, confirmPassword: String
  ) {
    if (isFormValid(name, email, birthDate, password, confirmPassword)) {
      viewModel.setSignUpInfo(name, email, birthDate, password)
      findNavController().navigate(
        SignUpFragmentDirections.actionSignUpFragmentToSelectGenderFragment()
      )
    } else {
      setFormErrorMessage()
    }
  }

  companion object {

    private const val BIRTH_DATE_PICKER = "Birth Date Picker"

    @JvmStatic
    fun newInstance() = SignUpFragment()
  }
}