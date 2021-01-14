package com.future.tailormade.tailor_app.feature.orderDetail.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.core.model.ui.order.OrderDesignUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailMeasurementUiModel
import com.future.tailormade.tailor_app.databinding.ActivityOrderDetailBinding
import com.future.tailormade.tailor_app.feature.orderDetail.viewModel.OrderDetailViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActivity : BaseActivity() {

  companion object {
    private const val PARAM_ORDER_DETAIL_ID = "ORDER_DETAIL_ID"
  }

  private lateinit var binding: ActivityOrderDetailBinding

  private val viewModel: OrderDetailViewModel by viewModels()

  private var orderDetailId: String? = null

  override fun getScreenName(): String = orderDetailId ?: "Order Detail"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityOrderDetailBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarOrderDetail
    setContentView(binding.root)

    orderDetailId = intent?.getStringExtra(PARAM_ORDER_DETAIL_ID)
    setupToolbar(getScreenName())
    orderDetailId?.let { id ->
      viewModel.fetchOrderDetail(id)
    }
    viewModel.orderDetailUiModel.observe(this, { orderDetail ->
      setupOrderInfoData(orderDetail.id, orderDetail.orderedBy, orderDetail.orderDate)
      setupPaymentData(orderDetail.quantity, orderDetail.totalPrice, orderDetail.totalDiscount,
          orderDetail.paymentTotal)
      setupDesignDetailData(orderDetail.design)
      setupMeasurementDetailData(orderDetail.measurement)
      orderDetail.specialInstructions?.let {
        showSpecialInstruction(it)
      }
    })
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

      ImageLoader.loadImageUrl(this@OrderDetailActivity, design.image, imageViewOrder)

      root.setOnClickListener {
        Action.goToDesignDetail(this@OrderDetailActivity, design.id)
      }
    }
  }

  private fun setupDesignDiscount(price: String, discount: String) {
    with(binding.layoutDesignDetail) {
      textViewOrderPrice.remove()
      textViewOrderPriceBeforeDiscount.show()
      textViewOrderPriceAfterDiscount.show()

      textViewOrderPriceBeforeDiscount.text = price
      textViewOrderPriceAfterDiscount.text = discount
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
    with(binding.layoutCardOrderDetailOrderInfo) {
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
      textViewOrderDetailSpecialInstructions.show()
      textViewOrderDetailSpecialInstructionsTitle.show()
      textViewOrderDetailSpecialInstructions.text = instruction
    }
  }
}