package com.mta.blibli.tailormade_auth.feature.login.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.util.extension.isPhoneNumberValid
import com.mta.blibli.tailormade_auth.databinding.FragmentLoginBinding
import com.mta.blibli.tailormade_auth.feature.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNumber = it.getString(ARG_PHONE_NUMBER)
        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        with(binding) {
            buttonSubmitPhoneNumber.setOnClickListener {
                submitPhoneNumber(editTextPhoneNumber.text.toString())
            }

            editTextPhoneNumber.doOnTextChanged { text, _, _, count ->
                buttonSubmitPhoneNumber.isEnabled = text?.isNotBlank() ?: false && (count in 9..12)
            }

            textInputPhoneNumber.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER
                            || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                            || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                ) {
                    hideKeyboard()
                    submitPhoneNumber(editTextPhoneNumber.text.toString())
                    return@setOnKeyListener true
                }
                false
            }
        }

        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            binding.textInputPhoneNumber.error = it
        })
    }

    @InternalCoroutinesApi
    private fun submitPhoneNumber(phoneNumber: String) {
        viewModel.setPhoneNumber(phoneNumber)
        if (phoneNumber.isPhoneNumberValid()) {
            viewModel.submitPhoneNumber()
        }
    }

    companion object {

        const val ARG_PHONE_NUMBER = "phoneNumber"

        @JvmStatic
        fun newInstance(phoneNumber: String) = LoginFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PHONE_NUMBER, phoneNumber)
            }
        }
    }
}