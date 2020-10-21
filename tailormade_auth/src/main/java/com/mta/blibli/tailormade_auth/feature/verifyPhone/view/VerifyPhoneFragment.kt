package com.mta.blibli.tailormade_auth.feature.verifyPhone.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.mta.blibli.tailormade_auth.databinding.FragmentVerifyPhoneBinding
import com.mta.blibli.tailormade_auth.feature.login.view.LoginFragment
import com.mta.blibli.tailormade_auth.feature.verifyPhone.viewmodel.VerifyPhoneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class VerifyPhoneFragment : BaseFragment() {

    private lateinit var binding: FragmentVerifyPhoneBinding

    private val viewModel: VerifyPhoneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.setPhoneNumber(it.getString(LoginFragment.ARG_PHONE_NUMBER).orEmpty())
        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneBinding.inflate(layoutInflater, container, false)

        with(binding) {
            editTextVerificationCode.doOnTextChanged { text, _, _, _ ->
                buttonSubmitVerificationCode.isEnabled = text?.isNotBlank() ?: false
            }

            textInputVerificationCode.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER
                            || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                            || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                ) {
                    hideKeyboard()
                    viewModel.submitVerificationCode(editTextVerificationCode.text.toString())
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
            binding.textInputVerificationCode.error = it
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(phoneNumber: String) = VerifyPhoneFragment().apply {
            arguments = Bundle().apply {
                putString(LoginFragment.ARG_PHONE_NUMBER, phoneNumber)
            }
        }
    }
}