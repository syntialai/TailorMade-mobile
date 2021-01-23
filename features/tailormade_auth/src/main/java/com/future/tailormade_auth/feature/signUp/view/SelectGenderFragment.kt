package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.forEach
import androidx.fragment.app.activityViewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSelectGenderBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import com.future.tailormade_router.actions.UserAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SelectGenderFragment : BaseFragment(), View.OnClickListener {

  companion object {
    fun newInstance() = SelectGenderFragment()
  }

  private val viewModel: SignUpViewModel by activityViewModels()

  private lateinit var binding: FragmentSelectGenderBinding

  override fun getLogName(): String =
      "com.future.tailormade_auth.feature.signUp.view.SelectGenderFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentSelectGenderBinding.inflate(inflater, container, false)
    setupRadioButton()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onClick(view: View?) {
    with(binding) {
      when(view) {
        buttonSubmitGender -> submitGender(radioGroupSelectGender.checkedRadioButtonId)
      }
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.hasSignIn.observe(viewLifecycleOwner, {
      it?.let { hasSignIn ->
        if (hasSignIn) {
          goToMain()
        }
      }
    })
  }

  private fun goToMain() {
    context?.let {
      UserAction.goToMain(it)
    }
  }

  private fun setupRadioButton() {
    binding.radioGroupSelectGender.setOnCheckedChangeListener { group, checkedId ->
      val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
      selectedRadioButton.setTextColor(resources.getColor(R.color.color_primary_800))

      group.forEach {
        if (it != selectedRadioButton) {
          (it as RadioButton).setTextColor(resources.getColor(R.color.color_black_87))
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun submitGender(id: Int) {
    val selectedRadioButton =
        binding.radioGroupSelectGender.findViewById<RadioButton>(id)
    viewModel.setSignUpGender(selectedRadioButton.text.toString())
    viewModel.signUp()
  }
}