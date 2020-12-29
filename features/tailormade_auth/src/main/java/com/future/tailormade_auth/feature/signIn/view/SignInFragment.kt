package com.future.tailormade_auth.feature.signIn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.extension.text
import com.future.tailormade_auth.databinding.FragmentSignInBinding
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

  companion object {
    fun newInstance() = SignInFragment()
  }

  private lateinit var binding: FragmentSignInBinding

  private val viewModel: SignInViewModel by viewModels()

  override fun getLogName(): String =
      "com.future.tailormade_auth.feature.signIn.view.SignInFragment"

  override fun getScreenName(): String = "Sign In"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentSignInBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSignIn.setOnClickListener {
        submitEmailAndPassword(editTextEmailSignIn.text(), editTextPasswordSignIn.text())
      }

      buttonGoToSignUp.setOnClickListener {
        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
      }
    }

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    hideToolbar()
  }

  private fun isFormValid(email: String, password: String): Boolean =
    email.isNotBlank() && email.isEmailValid() && password.isNotBlank()

  private fun setFormErrorMessage() {
    with(binding) {
      textInputEmailSignIn.error = when {
        editTextEmailSignIn.text().isBlank() -> Constants.EMAIL_IS_EMPTY
        editTextEmailSignIn.text().isEmailValid().not() -> Constants.EMAIL_IS_NOT_VALID
        else -> null
      }

      textInputPasswordSignIn.error = when {
        editTextPasswordSignIn.text().isBlank() -> Constants.PASSWORD_IS_EMPTY
        else -> null
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun submitEmailAndPassword(email: String, password: String) {
    if (isFormValid(email, password)) {
      viewModel.signIn(email, password)
    } else {
      setFormErrorMessage()
    }
  }
}