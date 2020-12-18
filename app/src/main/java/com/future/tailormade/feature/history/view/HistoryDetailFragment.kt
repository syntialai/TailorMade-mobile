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
import com.future.tailormade.util.extension.show
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

	override fun setupFragmentObserver() {
		super.setupFragmentObserver()

		// TODO: Observe data
	}

	private fun setupDesignDetailData() {
		with(binding) {
			// TODO: set this method when layout is exist
		}
	}

	private fun setupMeasurementDetailData() {
		with(binding) {
			// TODO: set this method when layout is exist
		}
	}

	private fun setupOrderInfoData(orderId: String, orderedBy: String, orderDate: String) {
		with(binding.layoutCardHistoryDetailOrderInfo) {
			textViewHistoryDetailOrderId.text = orderId
			textViewHistoryDetailOrderBy.text = orderedBy
			textViewHistoryDetailOrderDate.text = orderDate
		}
	}

	private fun setupPaymentData(quantity: String, priceTotal: String, discountTotal: String,
			paymentTotal: String) {
		with(binding) {
			// TODO: set this method when layout is exist
		}
	}

	private fun showSpecialInstruction(instruction: String) {
		with(binding) {
			textViewHistoryDetailSpecialInstructionsTitle.show()
			textViewHistoryDetailSpecialInstructions.show()
			textViewHistoryDetailSpecialInstructions.text = instruction
		}
	}
}