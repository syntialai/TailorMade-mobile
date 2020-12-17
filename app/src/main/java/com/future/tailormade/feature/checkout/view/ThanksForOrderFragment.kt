package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.databinding.FragmentThanksForOrderBinding
import com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.image.ImageLoader

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

		hideUnusedButton()
		binding.buttonThankYouGoToHistory.setOnClickListener {
			// TODO: Go to history
		}

		return binding.root
	}

	private fun hideUnusedButton() {
		with(binding.layoutDesignDetail) {
			spinButtonNumber.remove()
			buttonDeleteOrder.remove()
			buttonCheckoutOrder.remove()
		}
	}

	private fun setupDesignDetailData(design: CartDesignUiModel) {
		with(binding.layoutDesignDetail) {
			textViewOrderTitle.text = design.title
			textViewOrderSize.text = design.size
			textViewOrderColor.text = design.color

			design.discount?.let { discount ->
				textViewOrderBeforeDiscount.text = design.price
				textViewOrderAfterDiscount.text = discount
			} ?: run {
				textViewOrderPrice.text = design.price
			}

			context?.let { context ->
				ImageLoader.loadImageUrl(context, design.image, imageViewOrder)
			}
		}
	}

	private fun setupPaymentData(data: CartUiModel) {
		with(binding) {
			textViewThankYouPaymentTotal.text = data.totalPayment
			textViewThankYouTotalDiscount.text = data.totalDiscount
		}
	}
}