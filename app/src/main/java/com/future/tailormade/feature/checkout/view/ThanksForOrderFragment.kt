package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentThanksForOrderBinding
import com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel

class ThanksForOrderFragment : BaseFragment() {

	companion object {
		fun newInstance() = ThanksForOrderFragment()
	}

	private lateinit var binding: FragmentThanksForOrderBinding

	private val viewModel: ThanksForOrderViewModel by viewModels()

	override fun getLogName(): String = "com.future.tailormade.feature.checkout.view.ThanksForOrderFragment"

	override fun getViewModel(): BaseViewModel = viewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentThanksForOrderBinding.inflate(inflater, container, false)
		return binding.root
	}
}