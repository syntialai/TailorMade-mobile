package com.mta.blibli.tailormade_auth.feature.signUp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.mta.blibli.tailormade_auth.databinding.FragmentSelectRoleBinding
import com.mta.blibli.tailormade_auth.feature.signUp.viewmodel.SignUpViewModel
import kotlinx.coroutines.InternalCoroutinesApi

class SelectRoleFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var binding: FragmentSelectRoleBinding

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectRoleBinding.inflate(layoutInflater, container, false)

        with(binding) {
            buttonSubmitRole.setOnClickListener {
                submitRole(radioGroupSelectRole.checkedRadioButtonId)
            }
        }

        return binding.root
    }

    @InternalCoroutinesApi
    private fun submitRole(id: Int) {
        val selectedRadioButton = binding.radioGroupSelectRole.findViewById<RadioButton>(id)
        viewModel.setSignUpRole(selectedRadioButton.text.toString())
        viewModel.signUp()
    }

    companion object {
        fun newInstance() = SelectRoleFragment()
    }
}