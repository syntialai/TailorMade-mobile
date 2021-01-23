package com.future.tailormade_auth.feature.signIn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.isEmailValid
import com.future.tailormade.util.extension.text
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSignInBinding
import com.future.tailormade_auth.feature.signIn.viewmodel.SignInViewModel
import com.future.tailormade_router.actions.UserAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SignInFragment : BaseFragment(), View.OnClickListener {

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
    return binding.root
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onClick(view: View?) {
    with(binding) {
      when (view) {
        buttonSignIn -> submitEmailAndPassword(editTextEmailSignIn.text(),
            editTextPasswordSignIn.text())
        buttonGoToSignUp -> findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
      }
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    hideToolbar()
    viewModel.userInfo.observe(viewLifecycleOwner, {
      it?.let {
        goToMain()
      }
    })
  }

  private fun goToMain() {
    context?.let {
      UserAction.goToMain(it)
    }
  }

  private fun isFormValid(email: String, password: String): Boolean =
    email.isNotBlank() && email.isEmailValid() && password.isNotBlank()

  private fun setFormErrorMessage() {
    with(binding) {
      textInputEmailSignIn.error = when {
        editTextEmailSignIn.text().isBlank() -> getString(R.string.email_is_empty)
        editTextEmailSignIn.text().isEmailValid().not() -> getString(R.string.email_is_invalid)
        else -> null
      }

      textInputPasswordSignIn.error = when {
        editTextPasswordSignIn.text().isBlank() -> getString(R.string.password_is_empty)
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