package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.toast.ToastHelper
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSignUpBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var birthDatePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        birthDatePicker = MaterialDatePicker.Builder.datePicker().build()

        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

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

            textInputPasswordSignUp.setEndIconOnClickListener {
                context?.let { context ->
                    if (textInputPasswordSignUp.endIconDrawable == ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_visibility_off
                        )
                    ) {
                        textInputPasswordSignUp.setEndIconDrawable(R.drawable.ic_visibility)
                        textInputPasswordSignUp.setEndIconActivated(true)
                    } else {
                        textInputPasswordSignUp.setEndIconDrawable(R.drawable.ic_visibility_off)
                        textInputPasswordSignUp.setEndIconActivated(false)
                    }
                }
            }

            textInputConfirmPasswordSignUp.setEndIconOnClickListener {
                context?.let { context ->
                    if (textInputConfirmPasswordSignUp.endIconDrawable == ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_visibility_off
                        )
                    ) {
                        textInputConfirmPasswordSignUp.setEndIconDrawable(R.drawable.ic_visibility)
                        textInputConfirmPasswordSignUp.setEndIconActivated(true)
                    } else {
                        textInputConfirmPasswordSignUp.setEndIconDrawable(R.drawable.ic_visibility_off)
                        textInputConfirmPasswordSignUp.setEndIconActivated(false)
                    }
                }
            }
        }

        setupObserver()

        return binding.root
    }

    private fun isFormValid(
        name: String,
        email: String,
        birthDate: String,
        password: String,
        confirmPassword: String
    ): Boolean =
        name.isNotBlank()
                && email.isNotBlank()
                && email.isEmailValid()
                && birthDate.isNotBlank()
                && password.isNotBlank()
                && password.length >= 8
                && confirmPassword.isNotBlank()
                && confirmPassword == password

    private fun setFormErrorMessage() {
        with(binding) {
            textInputNameSignUp.error = when {
                editTextNameSignUp.text.toString().isBlank() -> Constants.NAME_IS_EMPTY
                else -> null
            }

            textInputEmailSignUp.error = when {
                editTextEmailSignUp.text.toString().isBlank() -> Constants.EMAIL_IS_EMPTY
                editTextEmailSignUp.text.toString().isEmailValid()
                    .not() -> Constants.EMAIL_IS_NOT_VALID
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
                editTextConfirmPasswordSignUp.text.toString() != editTextPasswordSignUp.text.toString() ->
                    Constants.CONFIRM_PASSWORD_MUST_BE_SAME_WITH_PASSWORD
                else -> null
            }
        }
    }

    private fun setupObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner, { error ->
            if (error != null && context != null) {
                ToastHelper.showErrorToast(requireContext(), requireView(), error)
            }
        })
    }

    private fun showDatePicker() {
        birthDatePicker.show(parentFragmentManager, BIRTH_DATE_PICKER)
    }

    private fun submitForm(
        name: String,
        email: String,
        birthDate: String,
        password: String,
        confirmPassword: String
    ) {
        if (isFormValid(name, email, birthDate, password, confirmPassword)) {
            viewModel.setSignUpInfo(name, email, birthDate, password)
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