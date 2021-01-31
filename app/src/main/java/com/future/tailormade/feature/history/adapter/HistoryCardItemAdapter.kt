package com.future.tailormade.feature.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.core.model.ui.history.OrderDesignUiModel
import com.future.tailormade.core.model.ui.history.OrderUiModel
import com.future.tailormade.databinding.LayoutHistoryCardItemBinding
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.image.ImageLoader

class HistoryCardItemAdapter(private val onCardClickListener: (String) -> Unit) :
    ListAdapter<OrderUiModel, HistoryCardItemAdapter.HistoryCardItemViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<OrderUiModel>() {

      override fun areItemsTheSame(
          oldItem: OrderUiModel, newItem: OrderUiModel) = oldItem.id == newItem.id

      override fun areContentsTheSame(
          oldItem: OrderUiModel, newItem: OrderUiModel) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryCardItemViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_history_card_item, parent, false))

  override fun onBindViewHolder(holder: HistoryCardItemViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class HistoryCardItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = LayoutHistoryCardItemBinding.bind(itemView)
    private val context = itemView.context

    fun bind(data: OrderUiModel) {
      with(binding) {
        textViewHistoryOrderId.text = data.id
        textViewHistoryOrderDate.text = data.orderDate

        textViewHistoryDesignOrderedTitle.text = data.design.title
        textViewHistoryDesignOrderedColor.text = data.design.color
        textViewHistoryDesignOrderedSize.text = data.design.size

        ImageLoader.loadImageUrl(context, data.design.image, imageViewHistoryDesignOrdered)
        root.setOnClickListener {
          onCardClickListener.invoke(data.id)
        }
      }
      bindPriceData(data.design)
      bindLayoutTotalData(data)
    }

    private fun bindLayoutTotalData(data: OrderUiModel) {
      with(binding.layoutHistoryOrderedTotal) {
        textViewOrderQuantity.text = data.quantity
        textViewOrderTotalPrice.text = data.totalPrice
        textViewOrderTotalDiscount.text = data.totalDiscount
      }
    }

    private fun bindPriceData(design: OrderDesignUiModel) {
      with(binding) {
        design.discount?.let {
          showDiscount()
          textViewHistoryDesignOrderedPriceAfterDiscount.text = it
          textViewHistoryDesignOrderedPriceBeforeDiscount.text = design.price
          textViewHistoryDesignOrderedPriceBeforeDiscount.strikeThrough()
        } ?: run {
          textViewHistoryDesignOrderedPrice.text = design.price
        }
      }
    }

    private fun showDiscount() {
      with(binding) {
        textViewHistoryDesignOrderedPriceBeforeDiscount.show()
        textViewHistoryDesignOrderedPriceAfterDiscount.show()
        textViewHistoryDesignOrderedPrice.hide()
      }
    }
  }
}