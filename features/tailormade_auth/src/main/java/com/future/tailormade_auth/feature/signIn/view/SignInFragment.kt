package com.future.tailormade_auth.feature.signIn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade_auth.databinding.FragmentSignInBinding
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

  private lateinit var binding: FragmentSignInBinding

  private val viewModel: SignInViewModel by viewModels()

  override fun getScreenName(): String =
    "com.future.tailormade_auth.feature.signIn.view.SignInFragment"

  @InternalCoroutinesApi
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSignInBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSignIn.setOnClickListener {
        submitEmailAndPassword(
          editTextEmailSignIn.text.toString(),
          editTextPasswordSignIn.text.toString()
        )
      }

      buttonGoToSignUp.setOnClickListener {
        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
      }
    }

    return binding.root
  }

  private fun isFormValid(email: String, password: String): Boolean =
    email.isNotBlank() && email.isEmailValid() && password.isNotBlank()

  private fun setFormErrorMessage() {
    with(binding) {
      textInputEmailSignIn.error = when {
        editTextEmailSignIn.text.toString().isBlank() -> Constants.EMAIL_IS_EMPTY
        editTextEmailSignIn.text.toString().isEmailValid().not() -> Constants.EMAIL_IS_NOT_VALID
        else -> null
      }

      textInputPasswordSignIn.error = when {
        editTextPasswordSignIn.text.toString().isBlank() -> Constants.PASSWORD_IS_EMPTY
        else -> null
      }
    }
  }

  @InternalCoroutinesApi
  private fun submitEmailAndPassword(email: String, password: String) {
    if (isFormValid(email, password)) {
      viewModel.signIn(email, password)
    } else {
      setFormErrorMessage()
    }
  }

  companion object {

    @JvmStatic
    fun newInstance() = SignInFragment()
  }
}