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
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSignUpBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(), View.OnClickListener {

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
    binding.textInputBirthDateSignUp.setEndIconOnClickListener {
      showDatePicker()
    }
    return binding.root
  }

  override fun onClick(view: View?) {
    with(binding) {
      when (view) {
        buttonSubmitForm -> submitForm(editTextNameSignUp.text(), editTextEmailSignUp.text(),
            editTextBirthDateSignUp.text(), editTextPasswordSignUp.text(),
            editTextConfirmPasswordSignUp.text())
        buttonGoToSignIn -> findNavController().navigateUp()
        else -> null
      }
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()
    showToolbar()
  }

  private fun isFormValid(name: String, email: String, birthDate: String,
      password: String, confirmPassword: String): Boolean =
    name.isNotBlank() && email.isNotBlank() && email.isEmailValid()
            && birthDate.isNotBlank() && password.isNotBlank()
            && password.length >= Constants.MIN_PASSWORD_LENGTH
            && confirmPassword.isNotBlank() && confirmPassword == password

  private fun setFormErrorMessage() {
    with(binding) {
      textInputNameSignUp.error = when {
        editTextNameSignUp.text().isBlank() -> Constants.NAME_IS_EMPTY
        else -> null
      }

      textInputEmailSignUp.error = when {
        editTextEmailSignUp.text().isBlank() -> Constants.EMAIL_IS_EMPTY
        editTextEmailSignUp.text().isEmailValid().not() -> Constants.EMAIL_IS_NOT_VALID
        else -> null
      }

      textInputBirthDateSignUp.error = when {
        editTextBirthDateSignUp.text().isBlank() -> Constants.BIRTH_DATE_IS_NOT_SET
        else -> null
      }

      textInputPasswordSignUp.error = when {
        editTextPasswordSignUp.text().isBlank() -> Constants.PASSWORD_IS_EMPTY
        editTextPasswordSignUp.text().length <= 8 -> Constants.PASSWORD_IS_NOT_VALID
        else -> null
      }

      textInputConfirmPasswordSignUp.error = when {
        editTextConfirmPasswordSignUp.text().isBlank() -> Constants.CONFIRM_PASSWORD_IS_EMPTY
        editTextConfirmPasswordSignUp.text() != editTextPasswordSignUp.text() -> Constants.CONFIRM_PASSWORD_MUST_BE_SAME_WITH_PASSWORD
        else -> null
      }
    }
  }

  private fun setupDatePicker() {
    birthDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText(
        R.string.birth_date_picker_title_label).build()
    birthDatePicker.addOnPositiveButtonClickListener {
      binding.editTextBirthDateSignUp.setText(it.toDateString(Constants.DD_MMMM_YYYY, true))
    }
  }

  private fun showDatePicker() {
    birthDatePicker.show(parentFragmentManager, getString(R.string.birth_date_picker_label))
  }

  private fun submitForm(name: String, email: String, birthDate: String, password: String,
      confirmPassword: String) {
    if (isFormValid(name, email, birthDate, password, confirmPassword)) {
      viewModel.setSignUpInfo(name, email, birthDate, password)
      findNavController().navigate(
          SignUpFragmentDirections.actionSignUpFragmentToSelectGenderFragment())
    } else {
      setFormErrorMessage()
    }
  }
}