package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_auth.databinding.FragmentSelectGenderBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectGenderFragment : BaseFragment() {

  private val viewModel: SignUpViewModel by viewModels()

  private lateinit var binding: FragmentSelectGenderBinding

  override fun getScreenName(): String =
    "com.future.tailormade_auth.feature.signUp.view.SelectGenderFragment"

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSelectGenderBinding.inflate(
      layoutInflater, container,
      false
    )

    with(binding) {
      buttonSubmitGender.setOnClickListener {
        submitGender(radioGroupSelectGender.checkedRadioButtonId)
      }
    }

    return binding.root
  }

  private fun submitGender(id: Int) {
    val selectedRadioButton = binding.radioGroupSelectGender.findViewById<RadioButton>(
      id
    )
    viewModel.setSignUpGender(selectedRadioButton.text.toString())
    findNavController().navigate(
      SelectGenderFragmentDirections.actionSelectGenderFragmentToSelectRoleFragment()
    )
  }

  companion object {

    fun newInstance() = SelectGenderFragment()
  }
}