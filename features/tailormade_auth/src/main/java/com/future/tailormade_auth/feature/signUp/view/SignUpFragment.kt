package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.extension.text
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade.util.extension.validateInput
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSignUpBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

  companion object {
    fun newInstance() = SignUpFragment()
  }

  private val viewModel: SignUpViewModel by activityViewModels()

  private lateinit var binding: FragmentSignUpBinding

  private lateinit var birthDatePicker: MaterialDatePicker<Long>

  override fun getLogName(): String =
      "com.future.tailormade_auth.feature.signUp.view.SignUpFragment"

  override fun getScreenName(): String = "Sign Up"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    setupDatePicker()
    binding = FragmentSignUpBinding.inflate(inflater, container, false)
    with(binding) {
      textInputBirthDateSignUp.setEndIconOnClickListener {
        showDatePicker()
      }
      buttonSubmitForm.setOnClickListener {
        submitForm(editTextNameSignUp.text(), editTextEmailSignUp.text(),
            editTextBirthDateSignUp.text(), editTextPasswordSignUp.text(),
            editTextConfirmPasswordSignUp.text())
      }
      buttonGoToSignIn.setOnClickListener {
        findNavController().navigateUp()
      }
    }
    setupValidator()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()
    showToolbar()
  }

  private fun isConfirmPasswordValid(text: String) = Pair(
      text == binding.editTextPasswordSignUp.text(), getString(R.string.confirm_password_must_same))

  private fun isEmailValid(text: String) = Pair(text.isEmailValid(),
      getString(R.string.email_is_invalid))

  private fun isFormValid(name: String, email: String, birthDate: String,
      password: String, confirmPassword: String): Boolean =
    name.isNotBlank() && email.isNotBlank() && email.isEmailValid()
            && birthDate.isNotBlank() && password.isNotBlank()
            && password.length >= Constants.MIN_PASSWORD_LENGTH
            && confirmPassword.isNotBlank() && confirmPassword == password

  private fun isPasswordValid(text: String) = Pair(text.length >= Constants.MIN_PASSWORD_LENGTH,
      getString(R.string.password_is_invalid))

  private fun setFormErrorMessage() {
    with(binding) {
      textInputNameSignUp.error = when {
        editTextNameSignUp.text().isBlank() -> getString(R.string.name_is_empty)
        else -> null
      }

      textInputEmailSignUp.error = when {
        editTextEmailSignUp.text().isBlank() -> getString(R.string.email_is_empty)
        editTextEmailSignUp.text().isEmailValid().not() -> getString(R.string.email_is_invalid)
        else -> null
      }

      textInputBirthDateSignUp.error = when {
        editTextBirthDateSignUp.text().isBlank() -> getString(R.string.birth_date_is_not_set)
        else -> null
      }

      textInputPasswordSignUp.error = when {
        editTextPasswordSignUp.text().isBlank() -> getString(R.string.password_is_empty)
        editTextPasswordSignUp.text().length <= 8 -> getString(R.string.password_is_invalid)
        else -> null
      }

      textInputConfirmPasswordSignUp.error = when {
        editTextConfirmPasswordSignUp.text().isBlank() -> getString(
            R.string.confirm_password_is_empty)
        editTextConfirmPasswordSignUp.text() != editTextPasswordSignUp.text() -> getString(
            R.string.confirm_password_must_same)
        else -> null
      }
    }
  }

  private fun setupDatePicker() {
    birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(
        R.string.birth_date_picker_title_label).build()
    birthDatePicker.addOnPositiveButtonClickListener {
      binding.editTextBirthDateSignUp.setText(it.toDateString(Constants.DD_MMMM_YYYY, true))
      viewModel.setSignUpBirthDate(it)
    }
  }

  private fun setupValidator() {
    with(binding) {
      editTextNameSignUp.validateInput(textInputNameSignUp, getString(R.string.name_is_empty))
      editTextEmailSignUp.validateInput(textInputEmailSignUp, getString(R.string.email_is_empty),
          ::isEmailValid)
      editTextBirthDateSignUp.validateInput(textInputBirthDateSignUp,
          getString(R.string.birth_date_is_not_set))
      editTextPasswordSignUp.validateInput(textInputPasswordSignUp,
          getString(R.string.password_is_empty), ::isPasswordValid)
      editTextConfirmPasswordSignUp.validateInput(textInputConfirmPasswordSignUp,
          getString(R.string.confirm_password_is_empty), ::isConfirmPasswordValid)
    }
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager, getString(R.string.birth_date_picker_label))
  }

  private fun submitForm(name: String, email: String, birthDate: String, password: String,
      confirmPassword: String) {
    hideKeyboard()
    if (isFormValid(name, email, birthDate, password, confirmPassword)) {
      viewModel.setSignUpInfo(name, email, password)
      findNavController().navigate(
          SignUpFragmentDirections.actionSignUpFragmentToSelectGenderFragment())
    } else {
      setFormErrorMessage()
    }
  }
}