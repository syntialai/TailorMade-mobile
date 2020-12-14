package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentCheckoutBinding
import com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment() {

	companion object {
		fun newInstance() = CheckoutFragment()
	}

	private lateinit var binding: FragmentCheckoutBinding

	private val viewModel: CheckoutViewModel by viewModels()

	override fun getLogName() = "com.future.tailormade.feature.checkout.view.CheckoutFragment"

	override fun getViewModel(): BaseViewModel = viewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentCheckoutBinding.inflate(inflater, container, false)
		return binding.root
	}
}