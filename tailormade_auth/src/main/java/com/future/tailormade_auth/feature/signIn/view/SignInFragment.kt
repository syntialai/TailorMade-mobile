package com.future.tailormade_auth.feature.signIn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.toast.ToastHelper
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSignInBinding
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: SignInViewModel by viewModels()

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        with(binding) {
            buttonSignIn.setOnClickListener {
                submitEmailAndPassword(
                    editTextEmailSignIn.text.toString(),
                    editTextPasswordSignIn.text.toString()
                )
            }

            textInputPasswordSignIn.setEndIconOnClickListener {
                context?.let { context ->
                    if (textInputPasswordSignIn.endIconDrawable == ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_visibility_off
                        )
                    ) {
                        textInputPasswordSignIn.setEndIconDrawable(R.drawable.ic_visibility)
                        textInputPasswordSignIn.setEndIconActivated(true)
                    } else {
                        textInputPasswordSignIn.setEndIconDrawable(R.drawable.ic_visibility_off)
                        textInputPasswordSignIn.setEndIconActivated(false)
                    }
                }
            }
        }

        setupObserver()

        return binding.root
    }

    private fun setupObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            it?.let {
                ToastHelper.showErrorToast(requireContext(), requireView(), it)
            }
        })
    }

    @InternalCoroutinesApi
    private fun submitEmailAndPassword(email: String, password: String) {
        if (email.isBlank()) {
            binding.textInputEmailSignIn.error = Constants.EMAIL_IS_EMPTY
        } else if (!email.isEmailValid()) {
            binding.textInputEmailSignIn.error = Constants.EMAIL_IS_NOT_VALID
        } else if (password.isBlank()) {
            viewModel.setEmail(email)
            binding.textInputPasswordSignIn.error = Constants.PASSWORD_IS_EMPTY
        } else {
            viewModel.setPassword(password)
            viewModel.signIn()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}