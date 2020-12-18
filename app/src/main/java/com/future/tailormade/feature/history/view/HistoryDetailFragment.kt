package com.future.tailormade.feature.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentHistoryDetailBinding
import com.future.tailormade.feature.history.viewModel.HistoryDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryDetailFragment : BaseFragment() {

	companion object {
		fun newInstance() = HistoryDetailFragment()
	}

	private lateinit var binding: FragmentHistoryDetailBinding

	private val viewModel: HistoryDetailViewModel by viewModels()

	override fun getLogName() = "com.future.tailormade.feature.history.view.HistoryDetailFragment"

	override fun getViewModel(): BaseViewModel = viewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
		return binding.root
	}
}