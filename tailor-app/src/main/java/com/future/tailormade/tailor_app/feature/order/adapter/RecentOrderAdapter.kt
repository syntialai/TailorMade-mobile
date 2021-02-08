package com.future.tailormade.tailor_app.feature.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.core.model.ui.order.OrderDesignUiModel
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.databinding.LayoutCardOrderItemBinding
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.image.ImageLoader

class RecentOrderAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<OrderUiModel, RecentOrderAdapter.RecentOrderViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<OrderUiModel>() {
      override fun areItemsTheSame(
          oldItem: OrderUiModel, newItem: OrderUiModel) = oldItem.id == newItem.id

      override fun areContentsTheSame(
          oldItem: OrderUiModel, newItem: OrderUiModel) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecentOrderViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_order_item, parent, false))

  override fun onBindViewHolder(holder: RecentOrderViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class RecentOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val binding = LayoutCardOrderItemBinding.bind(itemView)
    private val context = itemView.context

    fun bind(data: OrderUiModel) {
      with(binding) {
        textViewOrderId.text = data.id
        textViewOrderDate.text = data.orderDate

        root.setOnClickListener {
          onClickListener.invoke(data.id)
        }
      }
      bindDesignData(data.design)
      bindOrderTotalData(data.quantity, data.totalPrice, data.totalDiscount, data.status)
    }

    private fun bindDesignData(design: OrderDesignUiModel) {
      with(binding) {
        textViewOrderedTitle.text = design.title
        textViewOrderedColor.text = design.color
        textViewOrderedSize.text = design.size

        design.discount?.let { discount ->
          groupDiscountPrice.show()
          textViewOrderedPrice.hide()

          textViewOrderedPriceDiscount.text = discount
          textViewOrderedDiscount.text = design.price
          textViewOrderedDiscount.strikeThrough()
        } ?: run {
          textViewOrderedPrice.text = design.price
        }

        ImageLoader.loadImageUrl(context, design.image, imageViewOrdered)
      }
    }

    private fun bindOrderTotalData(quantity: String, price: String, discount: String,
        status: String) {
      with(binding.layoutOrderTotal) {
        textViewOrderTotalStatusLabel.show()

        textViewOrderQuantity.text = quantity
        textViewOrderTotalPrice.text = price
        textViewOrderTotalDiscount.text = discount

        if (status == Constants.STATUS_ACCEPTED) {
          textViewOrderTotalStatusAccepted.show()
          textViewOrderTotalStatusAccepted.text = status
        } else {
          textViewOrderTotalStatusRejected.show()
          textViewOrderTotalStatusRejected.text = status
        }
      }
    }
  }
}