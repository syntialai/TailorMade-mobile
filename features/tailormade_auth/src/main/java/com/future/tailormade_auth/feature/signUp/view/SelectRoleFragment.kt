package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_auth.R
import com.future.tailormade_auth.databinding.FragmentSelectRoleBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SelectRoleFragment : BaseFragment() {

  private val viewModel: SignUpViewModel by viewModels()

  private lateinit var binding: FragmentSelectRoleBinding

  override fun getScreenName(): String =
    "com.future.tailormade_auth.feature.signUp.view.SelectRoleFragment"

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = FragmentSelectRoleBinding.inflate(inflater, container, false)

    with(binding) {
      buttonSubmitRole.setOnClickListener {
        submitRole(radioGroupSelectRole.checkedRadioButtonId)
      }

      radioGroupSelectRole.setOnCheckedChangeListener { group, checkedId ->
        val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
        selectedRadioButton.setTextColor(resources.getColor(R.color.color_primary_800))

        group.forEach {
          if (it != selectedRadioButton) {
            (it as RadioButton).setTextColor(resources.getColor(R.color.color_black_87))
          }
        }
      }
    }

    return binding.root
  }

  @ExperimentalCoroutinesApi
  @InternalCoroutinesApi
  private fun submitRole(id: Int) {
    val selectedRadioButton = binding.radioGroupSelectRole.findViewById<RadioButton>(id)
    if (selectedRadioButton == binding.radioButtonSelectRoleTailor) {
      viewModel.activateTailor()
    }
  }

  companion object {
    fun newInstance() = SelectRoleFragment()
  }
}