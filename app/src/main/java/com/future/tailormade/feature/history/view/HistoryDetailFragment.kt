package com.future.tailormade.feature.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.ui.history.OrderDesignUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailMeasurementUiModel
import com.future.tailormade.databinding.FragmentHistoryDetailBinding
import com.future.tailormade.feature.history.viewModel.HistoryDetailViewModel
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class HistoryDetailFragment : BaseFragment() {

  companion object {
    fun newInstance() = HistoryDetailFragment()
  }

  private lateinit var binding: FragmentHistoryDetailBinding

  private val args: HistoryDetailFragmentArgs by navArgs()
  private val viewModel: HistoryDetailViewModel by viewModels()

  override fun getLogName() = "com.future.tailormade.feature.history.view.HistoryDetailFragment"

  override fun getScreenName(): String = args.historyDetailId

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onNavigationIconClicked() {
    findNavController().navigateUp()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
    showSkeleton(binding.root, R.layout.layout_history_detail_skeleton)
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchHistoryDetails(args.historyDetailId)
    viewModel.orderDetailUiModel.observe(viewLifecycleOwner, {
      it?.let { orderDetail ->
        setupOrderInfoData(orderDetail.id, orderDetail.orderedBy, orderDetail.orderDate)
        setupPaymentData(orderDetail.quantity, orderDetail.totalPrice, orderDetail.totalDiscount,
            orderDetail.paymentTotal)
        setupDesignDetailData(orderDetail.design)
        setupMeasurementDetailData(orderDetail.measurement)
        orderDetail.specialInstructions?.let { instruction ->
          showSpecialInstruction(instruction)
        }
        hideSkeleton()
      }
    })
  }

  private fun goToDesignDetail(id: String) {
    context?.let { context ->
      Action.goToDesignDetail(context, id)
    }
  }

  private fun setupDesignDetailData(design: OrderDesignUiModel) {
    with(binding.layoutDesignDetail) {
      textViewOrderTitle.text = design.title
      textViewOrderColor.text = design.color
      textViewOrderSize.text = design.size

      design.discount?.let { discount ->
        setupDesignDiscount(design.price, discount)
      } ?: run {
        textViewOrderPrice.text = design.price
      }

      context?.let { context ->
        ImageLoader.loadImageUrl(context, design.image, imageViewOrder)
      }

      root.setOnClickListener {
        goToDesignDetail(design.id)
      }
    }
  }

  private fun setupDesignDiscount(price: String, discount: String) {
    with(binding.layoutDesignDetail) {
      textViewOrderPrice.hide()
      textViewOrderBeforeDiscount.show()
      textViewOrderAfterDiscount.show()

      textViewOrderAfterDiscount.text = discount
      textViewOrderBeforeDiscount.text = price
      textViewOrderBeforeDiscount.strikeThrough()
    }
  }

  private fun setupMeasurementDetailData(measurements: OrderDetailMeasurementUiModel) {
    with(binding.layoutSizeInformationDetail) {
      textViewSizeChest.text = measurements.chest
      textViewSizeWaist.text = measurements.waist
      textViewSizeHips.text = measurements.hips
      textViewSizeInseam.text = measurements.inseam
      textViewSizeNeckToWaist.text = measurements.neckToWaist
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
    with(binding.layoutPaymentInfo) {
      with(layoutCheckoutTotal) {
        textViewOrderQuantity.text = quantity
        textViewOrderTotalPrice.text = priceTotal
        textViewOrderTotalDiscount.text = discountTotal
      }
      textViewCheckoutPaymentTotal.text = paymentTotal
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