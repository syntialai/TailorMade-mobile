package com.future.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_auth.databinding.FragmentSelectGenderBinding
import com.future.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel

class SelectGenderFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSelectGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectGenderBinding.inflate(layoutInflater, container, false)

        with(binding) {
            buttonSubmitGender.setOnClickListener {
                submitGender(radioGroupSelectGender.checkedRadioButtonId)
            }
        }

        return binding.root
    }

    private fun submitGender(id: Int) {
        val selectedRadioButton = binding.radioGroupSelectGender.findViewById<RadioButton>(id)
        viewModel.setSignUpGender(selectedRadioButton.text.toString())
    }

    companion object {

        fun newInstance() = SelectGenderFragment()
    }
}