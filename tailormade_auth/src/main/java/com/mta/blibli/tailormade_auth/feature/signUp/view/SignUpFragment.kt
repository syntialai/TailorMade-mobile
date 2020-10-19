package com.mta.blibli.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.google.android.material.datepicker.MaterialDatePicker
import com.mta.blibli.tailormade_auth.databinding.FragmentSignUpBinding
import com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel

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
            textInputBirthDate.setEndIconOnClickListener {
                showDatePicker()
            }

            buttonSubmitForm.setOnClickListener {
                submitForm(
                    editTextName.text.toString(),
                    editTextEmail.text.toString(),
                    editTextBirthDate.text.toString()
                )
            }
        }

        setupObserver()

        return binding.root
    }

    private fun isFormValid(name: String, email: String, birthDate: String): Boolean =
        name.isNotBlank() && email.isNotBlank() && email.isEmailValid() && birthDate.isNotBlank()

    private fun setFormErrorMessage() {
        with(binding) {
            textInputName.error =
                if (editTextName.text.toString().isBlank()) Constants.NAME_IS_EMPTY else null
            textInputEmail.error = when {
                editTextEmail.text.toString().isBlank() -> Constants.EMAIL_IS_EMPTY
                !editTextEmail.text.toString().isEmailValid() -> Constants.EMAIL_IS_NOT_VALID
                else -> null
            }
            textInputBirthDate.error = if (editTextBirthDate.text.toString()
                    .isBlank()
            ) Constants.BIRTH_DATE_IS_NOT_SET else null
        }
    }

    private fun setupObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner, { error ->
            error?.let {
                context?.let { context ->
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showDatePicker() {
        birthDatePicker.show(parentFragmentManager, BIRTH_DATE_PICKER)
    }

    private fun submitForm(name: String, email: String, birthDate: String) {
        if (isFormValid(name, email, birthDate)) {
            viewModel.setSignUpInfo(name, email, birthDate)
        } else {
            setFormErrorMessage()
        }
    }

    companion object {

        private const val BIRTH_DATE_PICKER = "Birth Date Picker"

        fun newInstance() = SignUpFragment()
    }
}